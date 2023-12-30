package engine.render
import render_manager.RenderManager
import rendered_element.RenderedElement
import scala.collection.SortedMap

class RenderMaster {

  private var _managers: List[RenderManager] = Nil
  private var _layers: SortedMap[Float, Int] = SortedMap.empty

  def +=(element: RenderedElement): Unit = {

    // Add element to layer count, or create layer if none exist
    _layers.find(_._1 == element.layer) match
      case None =>
        _layers = _layers + (element.layer -> 1)
      case Some((l, _)) =>
        _layers = _layers.map((l1, n1) =>
          l1 -> (
            if (l1 == l) n1 + 1
            else n1
          )
        )

    // Add element to manager, creating manager if none
    _managers.find(m => element.isManager(m)) match {
      case Some(manager) =>
        manager += element
      case None =>
        val manager = element.newManager
        _managers = manager :: _managers
        manager += element
    }
  }

  def -=(element: RenderedElement): Unit = {

    // Remove element from layer count, and remove layer if count zero
    _layers.find(_._1 == element.layer) match {
      case Some((l, n)) =>
        if (n == 1)
          _layers = _layers.filterNot(_._1 == l)
        else
          _layers = _layers.map((l1, n1) =>
            l1 -> (
              if (l1 == l) n1 - 1
              else n1
            )
          )
      case None => throw new Exception("No layer found for element")
    }

    // Remove element from manager, removing manager if empty
    _managers.find(m => element.isManager(m)) match {
      case Some(manager) =>
        manager -= element
        if (manager.isEmpty)
          _managers = _managers.filterNot(_ == manager)
      case None => throw new Exception("No manager found for element")
    }
  }

  def render(): Unit = {
    for
      (l, n) <- _layers
      manager <- _managers
    do manager.renderLayer(l)
  }
}
