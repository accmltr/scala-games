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

final case class Image(path: String) {

  require(engine.io.fileExists(path), "Image file does not exist")

  private var _width: Int = 0
  private var _height: Int = 0
  private var _channels: Int = 0
  private var _data: ByteBuffer = null
  private var _texId: Int = 0
  private var _wrapMode: WrapMode = WrapMode.Repeat
  private var _filterMode: FilterMode = FilterMode.Linear

  def id: Int = _texId
  def width: Int = _width
  def height: Int = _height
  def channels: Int = _channels

  def render(window: Window): Unit = {

    import org.lwjgl.BufferUtils
    import org.lwjgl.opengl.GL11._
    import org.lwjgl.opengl.GL13._
    import org.lwjgl.opengl.GL15._
    import org.lwjgl.opengl.GL20._
    import org.lwjgl.opengl.GL30._
    import org.lwjgl.system.MemoryUtil
    import engine.render.shader.Uniform
    import engine.math.Vector4

    // Render Data
    val rd = renderData

    // Create and bind a VAO
    val vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    // Create and bind a VBO for the vertices
    val vboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, rd.vertices, GL_STATIC_DRAW)
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(0)

    // Create and bind a VBO for the indices
    val eboId = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, rd.indices, GL_STATIC_DRAW)

    // Unbind the VAO
    glBindVertexArray(0)

    // Bind shader program
    rd.shader.use()

    // Upload uniforms
    rd.shader.uploadUniforms(
      rd.extraUniforms ++ Map[String, Uniform](
        BuiltInUniforms.uRes.toString() -> window.resolution.toVector2,
        BuiltInUniforms.uLayer.toString() -> rd.layer,
        BuiltInUniforms.uColor.toString() -> Vector4(
          rd.color.r,
          rd.color.g,
          rd.color.b,
          rd.color.a
        ),
        BuiltInUniforms.uTrans.toString() -> rd.transform,
        BuiltInUniforms.uTime.toString() -> Time.current
      )
    )

    // Upload texture
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, _texId)

    // Set wireframe mode
    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

    // Draw
    glBindVertexArray(vaoId)
    glDrawElements(
      GL_TRIANGLES,
      rd.indices.length,
      GL_UNSIGNED_INT,
      0
    )
    glBindVertexArray(0)

    // Clean up
    glDeleteVertexArrays(vaoId)
    glDeleteBuffers(vboId)
    glDeleteBuffers(eboId)

    // Unbind texture
    glBindTexture(GL_TEXTURE_2D, 0)

    // Reset shader program
    import org.lwjgl.opengl.GL20._
    glUseProgram(0)
  }

  def loaded: Boolean = _texId != 0

  def load(): Unit = {
    // Generate texture on GPU
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
          "Error: (Texture) Unknown number of channels '" + channelsBuffer.get(
            0
          ) + "' in texture file: " + path
        )
      }
    } else {
      throw new Exception("Failed to load texture file: " + path)
    }

    import org.lwjgl.stb.STBImage.stbi_image_free
    // Free image from CPU memory
    stbi_image_free(image)
  }
}
