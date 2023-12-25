package engine.render.rendered_element

import engine.render.Color
import engine.render.shader.Shader
import engine.render.render_manager.RenderManager

final case class RenderedPerfectCircle(
    val radius: Float,
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
