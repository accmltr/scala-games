package engine.render.shader

import engine.io._
import java.io.IOException
import java.nio.FloatBuffer
import org.joml._
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import engine.math.{Vector2, Vector3, Vector4}
import java.nio.IntBuffer
import engine.math.Matrix3
import engine.math.Matrix4
import engine.render.shader.Uniform

/** Creates a compiled shader from a vertex and fragment shader.
  *
  * @param vertPath
  *   Path to vertex source file.
  * @param fragPath
  *   Path to fragment source file.
  */
final case class Shader(val vertPath: String, val fragPath: String) {
  private var _shaderProgramID: Int = 0
  private var _beingUsed: Boolean = false
  val vertexSource: String = engine.io.readTextFile(vertPath)
  val fragmentSource: String = engine.io.readTextFile(fragPath)

  def compiled: Boolean = _shaderProgramID != 0
  def shaderProgramID: Int = _shaderProgramID
  def beingUsed: Boolean = _beingUsed

  def compile(): Unit = {
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
    _shaderProgramID = glCreateProgram()
    glAttachShader(_shaderProgramID, vertexID)
    glAttachShader(_shaderProgramID, fragmentID)
    glLinkProgram(_shaderProgramID)

    // Delete vertex and fragment shaders
    glDeleteShader(vertexID)
    glDeleteShader(fragmentID)

    if (glGetProgrami(_shaderProgramID, GL_LINK_STATUS) == 0) {
      println(glGetShaderInfoLog(_shaderProgramID, 1024))
      assert(
        false,
        "Error: Vertex and/or fragment shader failed to link with GL program."
      )
    }

    glValidateProgram(_shaderProgramID)
    if (glGetProgrami(_shaderProgramID, GL_VALIDATE_STATUS) == 0) {
      println(glGetShaderInfoLog(_shaderProgramID, 1024))
      assert(
        false,
        "Error: Shader program validation failed. Check vertex and fragment shaders for errors."
      )
    }
  }

  def use(): Unit = {
    if (!_beingUsed) {
      // Bind shader program
      glUseProgram(_shaderProgramID)
      _beingUsed = true
    }
  }

  def detach(): Unit = {
    glUseProgram(0)
    _beingUsed = false
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
        case value: Matrix3      => uploadMatrix3(name, value)
        case value: Matrix4      => uploadMat4f(name, value)
        case _ =>
          throw new Exception(
            "Error: Invalid uniform type. Uniform must be a FloatBuffer, IntBuffer, Array[Float], Array[Int], Boolean, Float, Int, Double, Vector2, Vector3, Matrix3, or Matrix4."
          )
      }
    }
  }

  def uploadMat4f(varName: String, mat4: Matrix4): Unit = {
    val jomlMat4f: Matrix4f = mat4.toJomlMatrix4f
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
    jomlMat4f.get(matBuffer)
    glUniformMatrix4fv(varLocation, false, matBuffer)
  }

  def uploadMatrix3(varName: String, mat3: Matrix3): Unit = {
    val jomlMat3f = mat3.toJomlMatrix3f
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(9)
    jomlMat3f.get(matBuffer)
    glUniformMatrix3fv(varLocation, false, matBuffer)
  }

  def uploadVec4f(varName: String, vec: Vector4): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w)
  }

  def uploadVec3f(varName: String, vec: Vector3): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform3f(varLocation, vec.x, vec.y, vec.z)
  }

  def uploadVec2f(varName: String, vec: Vector2): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform2f(varLocation, vec.x, vec.y)
  }

  def uploadFloat(varName: String, value: Float): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1f(varLocation, value)
  }

  def uploadInt(varName: String, value: Int): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1i(varLocation, value)
  }

  def uploadTexture(varName: String, slot: Int): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1i(varLocation, slot)
  }

  def uploadIntBuffer(varName: String, buffer: IntBuffer): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1iv(varLocation, buffer)
  }

  def uploadFloatBuffer(varName: String, buffer: FloatBuffer): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1fv(varLocation, buffer)
  }

  def uploadIntArray(varName: String, array: Array[Int]): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1iv(varLocation, array)
  }

  def uploadFloatArray(varName: String, array: Array[Float]): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1fv(varLocation, array)
  }
}
