package engine.render

import org.lwjgl.glfw.{GLFW, GLFWImage}
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.system.{MemoryStack, MemoryUtil}
import org.lwjgl.BufferUtils
import java.nio.IntBuffer
import java.nio.ByteBuffer

final case class Image(path: String) {

  require(engine.io.fileExists(path), "Image file does not exist")

  private var _image: GLFWImage = null
  private var _width: IntBuffer = null
  private var _height: IntBuffer = null
  private var _channels: IntBuffer = null
  private var _imgData: ByteBuffer = null

  private[engine] def image: GLFWImage =
    if (_image == null) load()
    _image

  def width: Int =
    if (_width == null) load()
    _width.get(0)
  def height: Int =
    if (_width == null) load()
    _height.get(0)
  def channels: Int =
    if (_width == null) load()
    _channels.get(0)

  def load(): Unit = {
    if (_image != null)
      throw new RuntimeException("Image already loaded")

    _image = GLFWImage.malloc()
    _width = BufferUtils.createIntBuffer(1)
    _height = BufferUtils.createIntBuffer(1)
    _channels = BufferUtils.createIntBuffer(1)
    _imgData =
      org.lwjgl.stb.STBImage.stbi_load(path, _width, _height, _channels, 4)

    if (_imgData == null) {
      throw new RuntimeException(
        "Image loading failed: " + org.lwjgl.stb.STBImage.stbi_failure_reason()
      )
    }
    _image.set(_width.get(0), _height.get(0), _imgData)
  }

  def free(): Unit = {

    if (_image == null)
      throw new RuntimeException("Freeing non-loaded image.")

    stbi_image_free(_imgData)
    _image.free()

    _width.clear()
    _height.clear()
    _channels.clear()
    _imgData.clear()

    _width = null
    _height = null
    _channels = null
    _imgData = null
    _image = null
  }
}
