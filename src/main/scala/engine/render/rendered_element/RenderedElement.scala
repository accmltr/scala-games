package engine.render.rendered_element

import engine.render.render_manager.RenderManager
import java.nio.{FloatBuffer, IntBuffer}
import engine.render.Color
import engine.render.render_manager.ShapeRenderManager
import engine.render.shader.Shader
import engine.math.shapes.*
import engine.math.*
import org.lwjgl.BufferUtils
import engine.render.mesh.Mesh
import engine.render.Texture

// TODO: Implement new uniform types
type Uniforms = FloatBuffer | IntBuffer | Array[Float] | Array[Int] | Boolean |
  Float | Int | Double | Vector2 | Vector3 |
  Texture //| Vector4 | Matrix2 | Matrix3 | Matrix4

trait RenderedElement {
  val shader: Shader
  val tint: Color
  val layer: Float
  val uniforms: Map[String, Uniforms]

  if uniforms.contains("tint") then
    throw new IllegalArgumentException("Uniform name 'tint' is reserved")

  def isManager(manager: RenderManager): Boolean

  private[engine] def uploadUniforms(): Unit = {
    shader.uploadVec4f("tint", Vector4(tint.r, tint.g, tint.b, tint.a))
    uniforms.foreach { case (name, value) =>
      value match
        case _: Int     => shader.uploadInt(name, value.asInstanceOf[Int])
        case _: Float   => shader.uploadFloat(name, value.asInstanceOf[Float])
        case _: Vector2 => shader.uploadVec2f(name, value.asInstanceOf[Vector2])
        case _: Vector3 => shader.uploadVec3f(name, value.asInstanceOf[Vector3])
        case _: Array[Int] =>
          shader.uploadIntArray(name, value.asInstanceOf[Array[Int]])
        case _: Array[Float] =>
          shader.uploadFloatArray(name, value.asInstanceOf[Array[Float]])
        case _: IntBuffer =>
          shader.uploadIntBuffer(name, value.asInstanceOf[IntBuffer])
        case _: FloatBuffer =>
          shader.uploadFloatBuffer(name, value.asInstanceOf[FloatBuffer])
        case _ => throw new Exception("Invalid uniform type")
    }
  }
}

trait WorldSpaceElement() extends RenderedElement {
  val position: Vector2
  val rotation: Float
  val scale: Vector2

  if uniforms.contains("position") then
    throw new IllegalArgumentException("Uniform name 'position' is reserved")
  if uniforms.contains("rotation") then
    throw new IllegalArgumentException("Uniform name 'rotation' is reserved")
  if uniforms.contains("scale") then
    throw new IllegalArgumentException("Uniform name 'scale' is reserved")

  private[engine] override def uploadUniforms(): Unit = {
    super.uploadUniforms()
    shader.uploadVec2f("position", position)
    shader.uploadFloat("rotation", rotation)
    shader.uploadVec2f("scale", scale)
  }
}

trait SelfRenderedElement() extends RenderedElement {
  def render(): Unit
}

final case class RenderedMesh(
    override val shader: Shader,
    var mesh: Mesh,
    override val layer: Float = 0,
    override val tint: Color = Color.WHITE,
    override val uniforms: Map[String, Uniforms] = Map.empty
) extends RenderedElement {

  override def isManager(manager: RenderManager): Boolean = {
    manager match {
      case shapeManager: ShapeRenderManager => true
      case _                                => false
    }
  }
}
/*

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

  def uploadVec4f(varName: String, vec: Vector4f): Unit = {
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

  def uploadIntArray(varName: String, array: Array[Int]): Unit = {
    val varLocation: Int = glGetUniformLocation(_shaderProgramID, varName)
    use()
    glUniform1iv(varLocation, array)
  }
}
 */
