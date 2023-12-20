package engine.render

/* Java version:
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
 */

import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11._;
import org.lwjgl.stb.STBImage._;

class Texture(
    val filepath: String,
    val wrapMode: WrapMode = WrapMode.Repeat,
    val filterMode: FilterMode = FilterMode.Linear
) {

  private var _textureId: Int = 0
  private var _width: Int = 0
  private var _height: Int = 0
  private var _channels: Int = 0

  def init(): Unit = {
    // Generate texture on GPU
    _textureId = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, _textureId);

    // --------------------------------------------------------------------
    // SET TEXTURE PARAMETERS
    // --------------------------------------------------------------------

    // Repeat
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapMode.value);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapMode.value);

    // Linear filtering
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMode.value);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMode.value);

    // Load image
    val width: IntBuffer = BufferUtils.createIntBuffer(1);
    val height: IntBuffer = BufferUtils.createIntBuffer(1);
    val channels: IntBuffer = BufferUtils.createIntBuffer(1);

    val image: ByteBuffer = stbi_load(filepath, width, height, channels, 0);
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
          ) + "' in texture file: " + filepath
        );
      }
    } else {
      throw new Exception("Failed to load texture file: " + filepath);
    }

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
  def width: Int = _width
  def height: Int = _height
  def channels: Int = _channels

  override def toString(): String = {
    "Texture(\n path=\"" + filepath + "\",\n textureId=" + _textureId + ",\n width=" + _width + ",\n height=" + _height + ",\n channels=" + _channels + "\n)";
  }

  def dataEquals(other: Texture): Boolean = {
    _textureId == other._textureId &&
    _width == other._width &&
    _height == other._height &&
    _channels == other._channels
  }
  // override def equals(other: Any): Boolean =
  //   other match
  //     case other: Texture =>
  //       _textureId == other._textureId &&
  //       _width == other._width &&
  //       _height == other._height &&
  //       _channels == other._channels
  //     case _ => false
}

enum WrapMode(val value: Int):
  case Repeat extends WrapMode(GL_REPEAT)
  case Clamp extends WrapMode(GL_CLAMP)

enum FilterMode(val value: Int):
  case Linear extends FilterMode(GL_LINEAR)
  case Nearest extends FilterMode(GL_NEAREST)
