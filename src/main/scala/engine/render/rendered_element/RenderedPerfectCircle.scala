package engine.render.rendered_element

import engine.render.Color
import engine.render.shader.Shader
import engine.render.render_manager.RenderManager
import engine.math.Matrix3
import engine.math.Vector2

final case class RenderedPerfectCircle(
    val radius: Float,
    override val transform: Matrix3 = Matrix3.IDENTITY,
    override val layer: Float = 0,
    override val tint: Color = Color.WHITE
) extends RenderedElement {

  override val shader: Shader = {
    // TODO: Implement and add shader
    ???
  }

  override def isManager(manager: RenderManager): Boolean = {
    manager match {
      case _: SelfRenderedElement => true
      case _                      => false
    }
  }
}
