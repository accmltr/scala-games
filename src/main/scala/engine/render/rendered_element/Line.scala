package engine.render.rendered_element

import engine.render.render_manager.RenderManager
import engine.render.Color
import engine.render.shader_classes.Shader
import engine.math.Matrix3
import engine.render.mesh.Mesh
import engine.render.render_manager

final case class Line(
    override val shader: Shader,
    override val transform: Matrix3 = Matrix3.IDENTITY,
    var mesh: Mesh,
    override val layer: Float = 0,
    override val tint: Color = Color.WHITE,
    override val uniforms: Map[String, Uniforms] = Map.empty
) extends RenderedElement {

  override def newManager: RenderManager = render_manager.Line()

  override def isManager(manager: RenderManager): Boolean =
    manager match
      case _: render_manager.Line => true
      case _                      => false

}
