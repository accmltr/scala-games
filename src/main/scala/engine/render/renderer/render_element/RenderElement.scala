package engine.render.renderer.render_element

import engine.render.renderer.render_data.RenderData

trait RenderElement {
  private[renderer] def renderData: RenderData
}
