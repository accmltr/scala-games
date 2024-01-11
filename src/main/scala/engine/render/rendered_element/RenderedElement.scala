package engine.render.rendered_element

import engine.render.render_manager.*
import java.nio.{FloatBuffer, IntBuffer}
import engine.render.Color
import engine.render.shader_classes.Shader
import engine.math.shapes.*
import engine.math.*
import org.lwjgl.BufferUtils
import engine.render.mesh.Mesh
import engine.render.Texture

// TODO: Implement new uniform types
type Uniforms = FloatBuffer | IntBuffer | Array[Float] | Array[Int] | Boolean |
  Float | Int | Double | Vector2 | Vector3 | Matrix3 | Matrix4 |
  Texture //| Vector4 | Matrix2

trait RenderedElement {

  val shader: Shader
  val transform: Matrix3 = Matrix3.IDENTITY
  val tint: Color
  val layer: Float
  val uniforms: Map[String, Uniforms] = Map.empty

  def newManager: RenderManager

  if uniforms.contains("transform") then
    throw new IllegalArgumentException("Uniform name 'transform' is reserved")
  if uniforms.contains("tint") then
    throw new IllegalArgumentException("Uniform name 'tint' is reserved")

  def isManager(manager: RenderManager): Boolean

  private[engine] def uploadUniforms(): Unit = {
    shader.uploadMatrix3("transform", transform)
    shader.uploadVec4f("tint", Vector4(tint.r, tint.g, tint.b, tint.a))
    uniforms.foreach { case (name, value) =>
      value match
        case _: Int     => shader.uploadInt(name, value.asInstanceOf[Int])
        case _: Float   => shader.uploadFloat(name, value.asInstanceOf[Float])
        case _: Vector2 => shader.uploadVec2f(name, value.asInstanceOf[Vector2])
        case _: Vector3 => shader.uploadVec3f(name, value.asInstanceOf[Vector3])
        case _: Matrix3 =>
          shader.uploadMatrix3(name, value.asInstanceOf[Matrix3])
        case _: Matrix4 => shader.uploadMat4f(name, value.asInstanceOf[Matrix4])
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

final case class RenderedMesh(
    override val shader: Shader,
    override val transform: Matrix3 = Matrix3.IDENTITY,
    var mesh: Mesh,
    override val layer: Float = 0,
    override val tint: Color = Color.WHITE,
    override val uniforms: Map[String, Uniforms] = Map.empty
) extends RenderedElement {

  override def newManager: MeshRenderManager =
    MeshRenderManager()

  override def isManager(manager: RenderManager): Boolean = {
    manager match {
      case _: MeshRenderManager => true
      case _                    => false
    }
  }
}
