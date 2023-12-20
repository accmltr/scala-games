package engine.render.render_managers

trait RenderManager {
  def renderLayer(layer: Int): Unit
}
