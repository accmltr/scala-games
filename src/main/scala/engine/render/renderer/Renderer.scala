package engine.render.renderer

import engine.render.window.Window

trait Renderer(val window: Window) {

  /** Renders RenderData objects in order of their layer, from lowest to
    * highest.
    *
    * @param renderDatas
    *   Unordered list of RenderData objects. Ordering is done automatically
    *   within this method.
    */
  def render(renderDatas: List[RenderData]): Unit
}
