package engine.render.window

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.system._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL14._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.opengl.GL45._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._
import engine.Time
import engine.TimeImplicits.given
import engine.input.{MouseListener, KeyListener}
import engine.math.Vector2
import scala.collection.mutable.Queue
import engine.math.Vector3
import engine.render.window.Resolution
import engine.render.window.FpsStats
import engine.input.{KeyListener, MouseListener}
import java.nio.ByteBuffer
import java.nio.IntBuffer
import org.lwjgl.stb.STBImage.stbi_load
import org.lwjgl.stb.STBImage.stbi_failure_reason

class Window(
    private var _title: String,
    mouseListener: MouseListener,
    keyListener: KeyListener,
    initCallback: () => Unit,
    postRenderCallback: Float => Unit
) {
  // Private Fields
  private var _windowId: Long = -1;
  private var _deltaTime = 0.0f
  private var _backgroundColor = Vector3(0.0f, 0.0f, 0.0f)
  private var _resolution = Resolution(800, 600)
  private var _vsync = false
  private var _maximized = false
  private var _centered = true

  val fpsStats = FpsStats()

  // Public Accessors
  def windowId: Long = _windowId
  def isInitialized: Boolean = _windowId != -1
  def deltaTime: Double = _deltaTime
  def backgroundColor: Vector3 = _backgroundColor
  def backgroundColor_=(color: Vector3): Unit = _backgroundColor = color
  def title: String = _title
  def title_=(title: String): Unit =
    _title = title
    if (isInitialized)
      glfwSetWindowTitle(_windowId, _title)
  def resolution: Resolution = _resolution
  def resolution_=(size: Resolution): Unit = _resolution = size
  if (isInitialized)
    glfwSetWindowSize(_windowId, _resolution.width, _resolution.height)
  def aspect: Float = _resolution.toVector2.x / _resolution.toVector2.y
  def maximized: Boolean = _maximized
  def maximized_=(maximized: Boolean): Unit =
    _maximized = maximized
    if (isInitialized)
      if (maximized)
        glfwMaximizeWindow(_windowId)
      else
        glfwRestoreWindow(_windowId)
  def vsync: Boolean = _vsync
  def vsync_=(vsync: Boolean): Unit =
    if (isInitialized)
      glfwSwapInterval(if (vsync) 1 else 0)
    _vsync = vsync

  // Methods

  private[engine] def run(): Unit = {

    // Log that the game is running (and the version of LWJGL)
    println("Running Scala Games on LWJGL " + Version.getVersion + "!")

    _init()
    _runLoop()

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(_windowId)
    glfwDestroyWindow(_windowId)

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()

  }
  // Private Methods
  private def _init(): Unit = {
    // Setup an error callback
    GLFWErrorCallback.createPrint(System.err).set()

    // Initialize GLFW
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")

    // Configure GLFW
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
    if (_maximized)
      glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)

    // Create the window
    _windowId = glfwCreateWindow(
      _resolution.width,
      _resolution.height,
      _title,
      NULL,
      NULL
    )
    if (_windowId == NULL)
      throw new RuntimeException("Failed to create the GLFW window")

    // Set frame size callback
    glfwSetFramebufferSizeCallback(
      _windowId,
      (window: Long, width: Int, height: Int) => {
        glViewport(0, 0, width, height)
        _resolution = Resolution(width, height)
      }
    )

    // Set input callbacks
    glfwSetCursorPosCallback(
      _windowId,
      mouseListener.cursorPositionCallback
    )
    glfwSetMouseButtonCallback(
      _windowId,
      mouseListener.mouseButtonCallback
    )
    glfwSetScrollCallback(
      _windowId,
      mouseListener.scrollCallback
    )
    glfwSetKeyCallback(
      _windowId,
      keyListener.keyCallback
    )

    if (_centered)
      // Get the thread stack and push a new frame.
      stackPush() match {
        case stack =>
          val pWidth = stack.mallocInt(1) // int*
          val pHeight = stack.mallocInt(1) // int*

          // Get the window size passed to glfwCreateWindow
          glfwGetWindowSize(_windowId, pWidth, pHeight)

          // Get the resolution of the primary monitor
          val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

          // Center the window
          glfwSetWindowPos(
            _windowId,
            (vidmode.width() - pWidth.get(0)) / 2,
            (vidmode.height() - pHeight.get(0)) / 2
          )
      } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(_windowId)

    // Enable v-sync
    glfwSwapInterval(if (_vsync) 1 else 0)

    // Make the window visible
    glfwShowWindow(_windowId)
    glfwFocusWindow(_windowId)

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use. Hence methods like `glEnable` and
    // `glBlendFunc` are used after this line.
    GL.createCapabilities()

    // Enable blending
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

    // Initialize the game
    initCallback()
  }
  private def _runLoop(): Unit = {
    var lastTime: Float = Time

    // Set the clear color
    glClearColor(0, 0.5, 1, 1)

    while (!glfwWindowShouldClose(_windowId)) {
      // Poll events
      glfwPollEvents()

      // Swap the color buffers
      glfwSwapBuffers(_windowId)

      // Set the clear color
      val color = _backgroundColor
      glClearColor(color.x, color.y, color.z, 1)

      // Clear the framebuffer
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

      // Render the frame
      // postRenderCallback(_deltaTime)

      // Calculate delta time
      _deltaTime = Time - lastTime
      lastTime = Time

      // Update FPS Stats
      fpsStats.update(_deltaTime)

      // Display Stats
      var title = _title
      if (fpsStats.showCurrent)
        title += " | FPS: " + f"${fpsStats.current}%4.0f"
      if (fpsStats.showAvg)
        title += " | Avg FPS: " + f"${fpsStats.avg}%4.0f"
      title += " | Mouse: " + mouseListener.position.formatted(0) // Temp
      glfwSetWindowTitle(_windowId, title)

      // Trigger the renderUpdate
      postRenderCallback(_deltaTime)

      // EndFrame on Input
      mouseListener.endFrame()
      keyListener.endFrame()
    }
  }
  def requestAttention(): Unit = {
    glfwRequestWindowAttention(_windowId)
  }

  import org.lwjgl.glfw.GLFWImage
  private var _cursorPath: String = null
  private var _cursorWidth: Int = 0
  private var _cursorHeight: Int = 0
  private var _cursorChannels: Int = 0
  private var _cursorData: ByteBuffer = null
  private var _cursorHotspotX: Int = 0
  private var _cursorHotspotY: Int = 0
  private var _cursorGLFWImage: GLFWImage = null
  private var _cursorPointer: Long = 0
  def cursorPath: String = _cursorPath
  def cursorWidth: Int = _cursorWidth
  def cursorHeight: Int = _cursorHeight
  def cursorChannels: Int = _cursorChannels
  def cursorHotspotX: Int = _cursorHotspotX
  def cursorHotspotX_=(x: Int): Unit = {
    _cursorHotspotX = x
    if (_cursorPointer != 0)
      glfwDestroyCursor(_cursorPointer)
    _cursorPointer = glfwCreateCursor(_cursorGLFWImage, x, _cursorHotspotY)
    glfwSetCursor(_windowId, _cursorPointer)
  }
  def cursorHotspotY: Int = _cursorHotspotY
  def cursorHotspotY_=(y: Int): Unit = {
    _cursorHotspotY = y
    if (_cursorPointer != 0)
      glfwDestroyCursor(_cursorPointer)
    _cursorPointer = glfwCreateCursor(_cursorGLFWImage, _cursorHotspotX, y)
    glfwSetCursor(_windowId, _cursorPointer)
  }
  def setCursor(path: String, x: Int, y: Int): Unit = {

    require(engine.io.fileExists(path), "Cursor file does not exist")

    _cursorGLFWImage = GLFWImage.malloc()

    val wBuffer: IntBuffer = BufferUtils.createIntBuffer(1)
    val hBuffer: IntBuffer = BufferUtils.createIntBuffer(1)
    val channelsBuffer: IntBuffer = BufferUtils.createIntBuffer(1)
    val image: ByteBuffer =
      stbi_load(path, wBuffer, hBuffer, channelsBuffer, 0)
    if (image == null)
      throw new RuntimeException(
        "Failed to load cursor image: " + stbi_failure_reason()
      )

    _cursorPath = path
    _cursorWidth = wBuffer.get(0)
    _cursorHeight = hBuffer.get(0)
    _cursorChannels = channelsBuffer.get(0)
    _cursorData = image
    _cursorHotspotX = x
    _cursorHotspotY = y
    _cursorGLFWImage.set(_cursorWidth, _cursorHeight, _cursorData)

    // Set the cursor
    if (_cursorPointer != 0)
      glfwDestroyCursor(_cursorPointer)
    _cursorPointer = glfwCreateCursor(_cursorGLFWImage, x, y)
    glfwSetCursor(_windowId, _cursorPointer)
  }
  def clearCursor(): Unit = {
    if (_cursorPointer != 0)
      glfwDestroyCursor(_cursorPointer)
    glfwSetCursor(windowId, NULL)
    _cursorGLFWImage = null
  }
}
