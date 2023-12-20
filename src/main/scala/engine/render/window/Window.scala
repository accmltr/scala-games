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
import engine.render.window.ScreenSize
import engine.render.window.FpsStats
import engine.input.{KeyListener, MouseListener}

class Window(
    private var _title: String,
    mouseListener: MouseListener,
    keyListener: KeyListener,
    initCallback: () => Unit,
    postRenderCallback: Float => Unit
) {
  // Private Fields
  private var _window: Long = -1;
  private var _deltaTime = 0.0f
  private var _backgroundColor = Vector3(0.0f, 0.0f, 0.0f)
  private var _size = ScreenSize(800, 600)
  private var _vsync = false
  private var _maximized = false
  private var _centered = true

  val fpsStats = FpsStats()

  // Public Accessors
  def window: Long = _window
  def isInitialized: Boolean = _window != -1
  def deltaTime: Double = _deltaTime
  def backgroundColor: Vector3 = _backgroundColor
  def backgroundColor_=(color: Vector3): Unit = _backgroundColor = color
  def title: String = _title
  def title_=(title: String): Unit =
    _title = title
    if (isInitialized)
      glfwSetWindowTitle(_window, _title)
  def size: ScreenSize = _size
  def size_=(size: ScreenSize): Unit = _size = size
  if (isInitialized) glfwSetWindowSize(_window, _size.width, _size.height)
  def maximized: Boolean = _maximized
  def maximized_=(maximized: Boolean): Unit =
    _maximized = maximized
    if (isInitialized)
      if (maximized)
        glfwMaximizeWindow(_window)
      else
        glfwRestoreWindow(_window)
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
    glfwFreeCallbacks(_window)
    glfwDestroyWindow(_window)

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
    _window = glfwCreateWindow(_size.width, _size.height, _title, NULL, NULL)
    if (_window == NULL)
      throw new RuntimeException("Failed to create the GLFW window")

    // Set input callbacks
    glfwSetCursorPosCallback(
      _window,
      mouseListener.cursorPositionCallback
    )
    glfwSetMouseButtonCallback(
      _window,
      mouseListener.mouseButtonCallback
    )
    glfwSetScrollCallback(
      _window,
      mouseListener.scrollCallback
    )
    glfwSetKeyCallback(
      _window,
      keyListener.keyCallback
    )

    if (_centered)
      // Get the thread stack and push a new frame.
      stackPush() match {
        case stack =>
          val pWidth = stack.mallocInt(1) // int*
          val pHeight = stack.mallocInt(1) // int*

          // Get the window size passed to glfwCreateWindow
          glfwGetWindowSize(_window, pWidth, pHeight)

          // Get the resolution of the primary monitor
          val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

          // Center the window
          glfwSetWindowPos(
            _window,
            (vidmode.width() - pWidth.get(0)) / 2,
            (vidmode.height() - pHeight.get(0)) / 2
          )
      } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(_window)

    // Enable v-sync
    glfwSwapInterval(if (_vsync) 1 else 0)

    // Make the window visible
    glfwShowWindow(_window)
    glfwFocusWindow(_window)

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities()

    // Initialize the game
    initCallback()
  }
  private def _runLoop(): Unit = {
    var lastTime: Float = Time

    // Set the clear color
    glClearColor(0, 0.5, 1, 1)

    while (!glfwWindowShouldClose(_window)) {
      // Poll events
      glfwPollEvents()

      // Swap the color buffers
      glfwSwapBuffers(_window)

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
      glfwSetWindowTitle(_window, title)

      // Trigger the renderUpdate
      postRenderCallback(_deltaTime)

      // EndFrame on Input
      mouseListener.endFrame()
      keyListener.endFrame()
    }
  }
  def requestAttention(): Unit = {
    glfwRequestWindowAttention(_window)
  }

}
