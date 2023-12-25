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

class Shader(vert: VertexShader, frag: FragmentShader) {
  private var _shaderProgramID: Int = 0
  private var _beingUsed: Boolean = false
  val vertexSource: String = vert.source
  val fragmentSource: String = frag.source

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

    // Delete shaders
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

  def uploadMat4f(varName: String, mat4: Matrix4f): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
    mat4.get(matBuffer)
    glUniformMatrix4fv(varLocation, false, matBuffer)
  }

  def uploadMat3f(varName: String, mat3: Matrix3f): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(9)
    mat3.get(matBuffer)
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

object Shader {
  def apply(vertexPath: String, fragmentPath: String): Shader = {
    val vertexShader = VertexShader(vertexPath)
    val fragmentShader = FragmentShader(fragmentPath)
    new Shader(vertexShader, fragmentShader)
  }
}
