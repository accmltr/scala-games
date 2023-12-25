package engine.render.render_manager
import engine.render.rendered_element.RenderedElement

trait RenderManager {

  def addElement(element: RenderedElement): Unit

  def removeElement(element: RenderedElement): Unit

  def renderLayer(layer: Float): Unit
}
