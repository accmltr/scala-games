package engine.rendering.shader

import engine.io._
import java.io.IOException
import java.nio.FloatBuffer
import org.joml._
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import engine.math.Vector3
import engine.math.Vector2

class Shader(vertexPath: String, fragmentPath: String) {
  private var shaderProgramID: Int = 0
  private var beingUsed: Boolean = false
  private var vertexSource: String = _
  private var fragmentSource: String = _

  // Read shader sources
  try {
    vertexSource = readTextFile(vertexPath)
  } catch {
    case e: IOException =>
      e.printStackTrace()
      assert(false, s"Error: Could not read vertex shader: '$vertexPath'")
  }
  try {
    fragmentSource = readTextFile(fragmentPath)
  } catch {
    case e: IOException =>
      e.printStackTrace()
      assert(false, s"Error: Could not read vertex shader: '$fragmentPath'")
  }

  def compile(): Unit = {
    // Compile and link vertex shader
    val vertexID = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vertexID, vertexSource)
    glCompileShader(vertexID)

    // Compile and link fragment shader
    val fragmentID = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fragmentID, fragmentSource)
    glCompileShader(fragmentID)

    // Create shader program and link shaders
    shaderProgramID = glCreateProgram()
    glAttachShader(shaderProgramID, vertexID)
    glAttachShader(shaderProgramID, fragmentID)
    glLinkProgram(shaderProgramID)

    // Delete shaders
    glDeleteShader(vertexID)
    glDeleteShader(fragmentID)

    if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) == 0) {
      println(glGetShaderInfoLog(shaderProgramID, 1024))
      assert(
        false,
        "Error: Vertex and/or fragment shader failed to link with GL program."
      )
    }

    glValidateProgram(shaderProgramID)
    if (glGetProgrami(shaderProgramID, GL_VALIDATE_STATUS) == 0) {
      println(glGetShaderInfoLog(shaderProgramID, 1024))
      assert(
        false,
        "Error: Shader program validation failed. Check vertex and fragment shaders for errors."
      )
    }
  }

  def use(): Unit = {
    if (!beingUsed) {
      // Bind shader program
      glUseProgram(shaderProgramID)
      beingUsed = true
    }
  }

  def detach(): Unit = {
    glUseProgram(0)
    beingUsed = false
  }

  def uploadMat4f(varName: String, mat4: Matrix4f): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
    mat4.get(matBuffer)
    glUniformMatrix4fv(varLocation, false, matBuffer)
  }

  def uploadMat3f(varName: String, mat3: Matrix3f): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(9)
    mat3.get(matBuffer)
    glUniformMatrix3fv(varLocation, false, matBuffer)
  }

  def uploadVec4f(varName: String, vec: Vector4f): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w)
  }

  def uploadVec3f(varName: String, vec: Vector3): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform3f(varLocation, vec.x, vec.y, vec.z)
  }

  def uploadVec2f(varName: String, vec: Vector2): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform2f(varLocation, vec.x, vec.y)
  }

  def uploadFloat(varName: String, value: Float): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform1f(varLocation, value)
  }

  def uploadInt(varName: String, value: Int): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform1i(varLocation, value)
  }

  def uploadTexture(varName: String, slot: Int): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform1i(varLocation, slot)
  }

  def uploadIntArray(varName: String, array: Array[Int]): Unit = {
    val varLocation: Int = glGetUniformLocation(shaderProgramID, varName)
    use()
    glUniform1iv(varLocation, array)
  }
}
