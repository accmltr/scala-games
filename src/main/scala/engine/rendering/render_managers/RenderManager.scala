package engine.rendering.render_managers

trait RenderManager {
  def renderLayer(layer: Int): Unit
}
