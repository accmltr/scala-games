package engine.render.render_manager
import engine.render.render_manager.RenderManager
import engine.render.rendered_element.RenderedElement

case class ShapeRenderManager() extends RenderManager {

  override def addElement(element: RenderedElement): Unit = ???

  override def removeElement(element: RenderedElement): Unit = ???

  override def renderLayer(layer: Float): Unit = ???

}
