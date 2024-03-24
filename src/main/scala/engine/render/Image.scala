package engine.render

import org.lwjgl.glfw.{GLFW, GLFWImage}
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.system.{MemoryStack, MemoryUtil}
import org.lwjgl.BufferUtils
import java.nio.IntBuffer
import java.nio.ByteBuffer
import engine.render.window.Window
import engine.render.renderer.RenderData
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import engine.render.renderer.BuiltInUniforms
import engine.Time
import engine.render.shader.Shader
import engine.math.Matrix3

/** Represents an image file that is loaded into GPU memory.
  *
  * It can be loaded and unloaded from GPU memory. This is done automatically
  * when the image is used by the engine for the first time, but can be done
  * manually with the `load` and `unload` methods, as long as a valid OpenGL
  * context is available, i.e. when a `Window` has been intialized.
  *
  * @param path
  */
final case class Image(path: String) {

  require(engine.io.fileExists(path), "Image file does not exist")

  private var _width: Int = 0
  private var _height: Int = 0
  private var _channels: Int = 0
  private var _texId: Int = 0
  private var _wrapMode: WrapMode = WrapMode.Repeat
  private var _filterMode: FilterMode = FilterMode.Linear

  def id: Int =
    if (!loaded) load()
    _texId
  def width: Int =
    if (!loaded) load()
    _width
  def height: Int =
    if (!loaded) load()
    _height
  def channels: Int =
    if (!loaded) load()
    _channels

  def loaded: Boolean = _texId != 0

  def load(): Unit = {
    require(
      GLFW.glfwGetCurrentContext() != MemoryUtil.NULL,
      "OpenGL context not available"
    )

    // Generate texture on GPU and get the texture ID
    _texId = glGenTextures()
    glBindTexture(GL_TEXTURE_2D, _texId)

    // --------------------------------------------------------------------
    // SET TEXTURE PARAMETERS
    // --------------------------------------------------------------------

    // Repeat
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, _wrapMode.value)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, _wrapMode.value)

    // Linear filtering
    glTexParameteri(
      GL_TEXTURE_2D,
      GL_TEXTURE_MIN_FILTER,
      _filterMode.value
    )
    glTexParameteri(
      GL_TEXTURE_2D,
      GL_TEXTURE_MAG_FILTER,
      _filterMode.value
    )

    // Load image
    val wBuffer: IntBuffer = BufferUtils.createIntBuffer(1)
    val hBuffer: IntBuffer = BufferUtils.createIntBuffer(1)
    val channelsBuffer: IntBuffer = BufferUtils.createIntBuffer(1)

    import org.lwjgl.stb.STBImage.stbi_load
    val image: ByteBuffer =
      stbi_load(path, wBuffer, hBuffer, channelsBuffer, 0)
    if (image != null) {

      // Store image data
      _width = wBuffer.get(0)
      _height = hBuffer.get(0)
      _channels = channelsBuffer.get(0)

      // Send image data to GPU
      if (channelsBuffer.get(0) == 3) {
        // RGB image
        glTexImage2D(
          GL_TEXTURE_2D,
          0,
          GL_RGB,
          wBuffer.get(0),
          hBuffer.get(0),
          0,
          GL_RGB,
          GL_UNSIGNED_BYTE,
          image
        )
      } else if (channelsBuffer.get(0) == 4) {
        // RGBA image
        glTexImage2D(
          GL_TEXTURE_2D,
          0,
          GL_RGBA,
          wBuffer.get(0),
          hBuffer.get(0),
          0,
          GL_RGBA,
          GL_UNSIGNED_BYTE,
          image
        )
      } else {
        throw new Exception(
          "Unknown number of color channels in texture file: " + path +
            ". Expected 3 or 4, but found: " + channelsBuffer.get(0) + "."
        )
      }
    } else {
      throw new Exception("Failed to load texture file: " + path)
    }

    import org.lwjgl.stb.STBImage.stbi_image_free
    // Free image from CPU memory
    stbi_image_free(image)
  }

  def unload(): Unit = {
    require(loaded, "Trying to unload an image that is not loaded")
    if (_texId != 0) {
      glDeleteTextures(_texId)
      _texId = 0
    }
  }
}
