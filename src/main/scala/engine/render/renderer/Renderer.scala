package engine.render.renderer

import engine.render.window.Window

trait Renderer(val window: Window) {
  def render(element: List[RenderData]): Unit
}
