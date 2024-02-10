package engine.render.renderer.render_element

import engine.render.Image
import engine.render.renderer.RenderData
import engine.math.Matrix3
import engine.render.shader.Shader
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import java.nio.IntBuffer
import java.nio.ByteBuffer
import engine.render.window.Window
import engine.render.renderer.BuiltInUniforms
import engine.Time

enum WrapMode(val value: Int):
  case Repeat extends WrapMode(GL_REPEAT)
  case Clamp extends WrapMode(GL_CLAMP)

enum FilterMode(val value: Int):
  case Linear extends FilterMode(GL_LINEAR)
  case Nearest extends FilterMode(GL_NEAREST)

final case class Sprite private (private val image: Image)
    extends RenderElement {

  private val path: String = image.path

  require(engine.io.fileExists(path), "Image file does not exist")

  private var _textureId: Int = 0
  private var _width: Int = 0
  private var _height: Int = 0
  private var _channels: Int = 0
  private var _wrapMode: WrapMode = WrapMode.Repeat
  private var _filterMode: FilterMode = FilterMode.Linear

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
    glBindTexture(GL_TEXTURE_2D, _textureId)

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

  def loaded: Boolean = _textureId != 0

  def load(): Unit = {
    // Generate texture on GPU
    _textureId = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, _textureId);

    // --------------------------------------------------------------------
    // SET TEXTURE PARAMETERS
    // --------------------------------------------------------------------

    // Repeat
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, _wrapMode.value);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, _wrapMode.value);

    // Linear filtering
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, _filterMode.value);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, _filterMode.value);

    // Load image
    val width: IntBuffer = BufferUtils.createIntBuffer(1);
    val height: IntBuffer = BufferUtils.createIntBuffer(1);
    val channels: IntBuffer = BufferUtils.createIntBuffer(1);

    import org.lwjgl.stb.STBImage.stbi_load
    val image: ByteBuffer = stbi_load(path, width, height, channels, 0);
    if (image != null) {

      // Store image data
      _width = width.get(0);
      _height = height.get(0);
      _channels = channels.get(0);

      // Send image data to GPU
      if (channels.get(0) == 3) {
        // RGB image
        glTexImage2D(
          GL_TEXTURE_2D,
          0,
          GL_RGB,
          width.get(0),
          height.get(0),
          0,
          GL_RGB,
          GL_UNSIGNED_BYTE,
          image
        );
      } else if (channels.get(0) == 4) {
        // RGBA image
        glTexImage2D(
          GL_TEXTURE_2D,
          0,
          GL_RGBA,
          width.get(0),
          height.get(0),
          0,
          GL_RGBA,
          GL_UNSIGNED_BYTE,
          image
        );
      } else {
        throw new Exception(
          "Error: (Texture) Unknown number of channels '" + channels.get(
            0
          ) + "' in texture file: " + path
        );
      }
    } else {
      throw new Exception("Failed to load texture file: " + path);
    }

    import org.lwjgl.stb.STBImage.stbi_image_free
    // Free image from CPU memory
    stbi_image_free(image);
  }

  def bind(slot: Int = 0): Unit = {
    glBindTexture(GL_TEXTURE_2D, _textureId);
  }

  def unbind(): Unit = {
    glBindTexture(GL_TEXTURE_2D, 0);
  }

  def textureId: Int = _textureId
  def width: Int = image.width
  def height: Int = image.height
  def channels: Int = image.channels

  override def renderData: RenderData = {
    val vertices: Array[Float] = Array(
      0,
      0,
      width,
      0,
      width,
      height,
      0,
      height
    )
    val indices: Array[Int] = Array(0, 1, 2, 2, 3, 0)
    RenderData(
      shader = Shader(
        "src/main/scala/engine/render/shaders/vertex/default.vert",
        "src/main/scala/engine/render/shaders/fragment/sprite.frag"
      ),
      vertices = vertices,
      indices = indices,
      layer = layer,
      color = color,
      transform = Matrix3
        .transform(
          position,
          rotation,
          scale
        ),
      extraUniforms = Map(
        "uWidth" -> width,
        "uHeight" -> height
      )
    )
  }
}

object Sprite {
  def apply(path: String): Sprite = new Sprite(Image(path))
  def apply(image: Image): Sprite = new Sprite(image)
}
