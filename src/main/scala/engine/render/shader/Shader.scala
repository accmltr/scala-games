package engine.render.shader

import engine.io._
import java.io.IOException
import java.nio.FloatBuffer
import org.lwjgl.glfw.GLFW._
import org.joml._
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import engine.math.{Vector2, Vector3, Vector4}
import java.nio.IntBuffer
import engine.math.Matrix3
import engine.math.Matrix4
import engine.render.shader.Uniform
import engine.render.Image
import org.lwjgl.glfw.GLFWImage
import java.nio.ByteBuffer
import org.lwjgl.glfw.GLFW
import org.lwjgl.system.MemoryUtil

/** Creates a compiled shader from a vertex and fragment shader.
  *
  * @param vertPath
  *   Path to vertex source file.
  * @param fragPath
  *   Path to fragment source file.
  */
final case class Shader(val vertPath: String, val fragPath: String) {

  val vertexSource: String = engine.io
    .readTextFile(vertPath)
    .match
      case Some(source) => source
      case None =>
        throw new IllegalArgumentException(
          s"Vertex shader not found: $vertPath"
        )
  val fragmentSource: String = engine.io
    .readTextFile(fragPath)
    .match
      case Some(source) => source
      case None =>
        throw new IllegalArgumentException(
          s"Fragment shader not found: $fragPath"
        )

  private var _id: Int = -1

  def compiled: Boolean = _id != -1
  def id: Int = _id

  def isUsed(): Boolean = glGetInteger(GL_CURRENT_PROGRAM) == id

  private def _autoCompile(): Unit = {
    if !compiled then compile()
  }

  private[engine] def compile(): Unit = {
    _glcontextCheck()

    // Compile and link vertex shader
    val vertexID = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vertexID, vertexSource)
    glCompileShader(vertexID)

    // Compile and link fragment shader
    val fragmentID = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fragmentID, fragmentSource)
    glCompileShader(fragmentID)

    val compiled = new Array[Int](1)

    // Check for errors in vertex shader
    glGetShaderiv(vertexID, GL_COMPILE_STATUS, compiled)
    if (compiled(0) == 0) {
      val log = glGetShaderInfoLog(vertexID)
      throw new RuntimeException("Failed to compile vertex shader: " + log)
    }

    // Check for errors in fragment shader
    glGetShaderiv(fragmentID, GL_COMPILE_STATUS, compiled)
    if (compiled(0) == 0) {
      val log = glGetShaderInfoLog(fragmentID)
      throw new RuntimeException("Failed to compile fragment shader: " + log)
    }

    // Create shader program and link shaders
    val programId = glCreateProgram()
    glAttachShader(programId, vertexID)
    glAttachShader(programId, fragmentID)
    glLinkProgram(programId)

    // Delete vertex and fragment shaders
    glDeleteShader(vertexID)
    glDeleteShader(fragmentID)

    if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
      println(glGetShaderInfoLog(programId, 1024))
      assert(
        false,
        "Vertex and/or fragment shader failed to link with GL program."
      )
    }

    glValidateProgram(programId)
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
      println(glGetShaderInfoLog(programId, 1024))
      assert(
        false,
        "Shader program validation failed. Check vertex and fragment shaders for errors."
      )
    }

    _id = programId
  }

  def use(): Unit = {
    _glcontextCheck()
    _autoCompile()

    // Bind shader program
    glUseProgram(id)
  }

  def detach(): Unit = {
    _glcontextCheck()
    _autoCompile()

    glUseProgram(0)
  }

  private def _glcontextCheck(): Unit = {
    if glfwGetCurrentContext() == 0L then
      throw new Exception(
        "Game has not yet been initialized."
      )
  }

  /** Uploads a map of uniforms to the shader by matching each uniform to its
    * corresponding upload method.
    *
    * This is a convenience method for uploading multiple uniforms at once.
    *
    * @param uniforms
    */
  def uploadUniforms(uniforms: Map[String, Uniform]): Unit = {
    uniforms.foreach { case (name, value) =>
      value match {
        case buffer: FloatBuffer => uploadFloatBuffer(name, buffer)
        case buffer: IntBuffer   => uploadIntBuffer(name, buffer)
        case array: Array[Float] => uploadFloatArray(name, array)
        case array: Array[Int]   => uploadIntArray(name, array)
        case value: Boolean      => uploadInt(name, if (value) 1 else 0)
        case value: Float        => uploadFloat(name, value)
        case value: Int          => uploadInt(name, value)
        case value: Double       => uploadFloat(name, value.toFloat)
        case value: Vector2      => uploadVec2f(name, value)
        case value: Vector3      => uploadVec3f(name, value)
        case value: Vector4      => uploadVec4f(name, value)
        case value: Matrix3      => uploadMatrix3(name, value)
        case value: Matrix4      => uploadMat4f(name, value)
        case (texUnit: Int, image: Image) => uploadImage(name, texUnit, image)
        case null =>
          throw new Exception(
            "Invalid uniform type. Uniform must be a FloatBuffer, IntBuffer, Array[Float], Array[Int], Boolean, Float, Int, Double, Vector2, Vector3, Matrix3, or Matrix4."
          )
      }
    }
  }

  def uploadImage(varName: String, textureUnit: Int, image: Image): Unit = {
    _uniformUsedCheck()
    require(
      textureUnit >= 0 && textureUnit < Shader.maxTextureUnits,
      s"Texture unit out of range. Must be between 0 and ${Shader.maxTextureUnits - 1}, inclusive."
    )
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glActiveTexture(GL_TEXTURE0 + textureUnit)
    glBindTexture(GL_TEXTURE_2D, image.id)
    glUniform1i(varLocation, textureUnit)
  }

  def uploadMat4f(varName: String, mat4: Matrix4): Unit = {
    _uniformUsedCheck()
    val jomlMat4f: Matrix4f = mat4.toJomlMatrix4f
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
    jomlMat4f.get(matBuffer)
    glUniformMatrix4fv(varLocation, false, matBuffer)
  }

  def uploadMatrix3(varName: String, mat3: Matrix3): Unit = {
    _uniformUsedCheck()
    val jomlMat3f = mat3.toJomlMatrix3f
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(9)
    jomlMat3f.get(matBuffer)
    glUniformMatrix3fv(varLocation, false, matBuffer)
  }

  def uploadVec4f(varName: String, vec: Vector4): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w)
  }

  def uploadVec3f(varName: String, vec: Vector3): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform3f(varLocation, vec.x, vec.y, vec.z)
  }

  def uploadVec2f(varName: String, vec: Vector2): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform2f(varLocation, vec.x, vec.y)
  }

  def uploadFloat(varName: String, value: Float): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform1f(varLocation, value)
  }

  def uploadInt(varName: String, value: Int): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform1i(varLocation, value)
  }

  def uploadIntBuffer(varName: String, buffer: IntBuffer): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform1iv(varLocation, buffer)
  }

  def uploadFloatBuffer(varName: String, buffer: FloatBuffer): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform1fv(varLocation, buffer)
  }

  def uploadIntArray(varName: String, array: Array[Int]): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform1iv(varLocation, array)
  }

  def uploadFloatArray(varName: String, array: Array[Float]): Unit = {
    _uniformUsedCheck()
    val varLocation: Int = glGetUniformLocation(id, varName)
    use()
    glUniform1fv(varLocation, array)
  }

  private def _uniformUsedCheck(): Unit = {
    if !isUsed() then
      throw new Exception(
        "Shader must be bound before uploading uniforms."
      )
  }

  override def equals(x: Any): Boolean = x match {
    case Shader(vertPath, fragPath) =>
      this.vertPath == vertPath && this.fragPath == fragPath
    case _ => false
  }
}

object Shader {
  private var _maxTextureUnits: Int = -1
  def maxTextureUnits: Int =
    // Make sure the OpenGL context is available
    if GLFW.glfwGetCurrentContext() == MemoryUtil.NULL then
      throw new Exception(
        "OpenGL context not available"
      )
    else
      if (_maxTextureUnits == -1)
        _maxTextureUnits = glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS)
      _maxTextureUnits
}
