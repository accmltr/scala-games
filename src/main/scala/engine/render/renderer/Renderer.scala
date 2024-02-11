package engine.render.renderer

import engine.render.window.Window
import engine.render.shader.Shader
import engine.render.renderer.render_element.Sprite

trait Renderer(val window: Window) {

  /** Renders RenderData objects in order of their layer, from lowest to
    * highest.
    *
    * @param renderDatas
    *   Unordered list of RenderData objects. Ordering is done automatically
    *   within this method.
    */
  def render(
      renderDatas: List[RenderData],
      wireframeMode: Boolean = false
  ): Unit

  // TEMP
  def renderSprites(sprites: List[Sprite]): Unit

  def applyPostProcessing(shaders: List[Shader]): Unit
}
