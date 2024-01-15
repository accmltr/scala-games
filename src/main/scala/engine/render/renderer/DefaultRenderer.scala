package engine.render.renderer

import engine.render.window.Window

final case class DefaultRenderer(
    override val window: Window
) extends Renderer(window) {
  override def render(element: List[RenderData]): Unit = {
    ???
  }
}
