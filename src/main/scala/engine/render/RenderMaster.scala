package engine.render
import render_manager.RenderManager
import rendered_element.RenderedElement

class RenderMaster {
  private var _managers: List[RenderManager] = Nil
  private var _layers: List[Float] = Nil

  def +=(manager: RenderManager): Unit = {
    _managers = manager :: _managers
  }

  def -=(manager: RenderManager): Unit = {
    _managers = _managers.filterNot(_ == manager)
  }

  def +=(element: RenderedElement): Unit = {
    _managers.find(m => element.isManager(m)) match {
      case Some(manager) =>
        _layers = _layers.filterNot(_ == element.layer) :+ element.layer
        manager.+=(element)
      case None => throw new Exception("No manager found for element")
    }
  }

  def -=(element: RenderedElement): Unit = {
    _managers.find(m => element.isManager(m)) match {
      case Some(manager) =>
        _layers = _layers.filterNot(_ == element.layer)
        manager.-=(element)
      case None => throw new Exception("No manager found for element")
    }
  }

  def render(): Unit = {
    _layers.foreach(l => _managers.foreach(_.renderLayer(l)))
  }
}
