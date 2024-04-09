package engine.core

import scala.reflect.ClassTag
import engine.math.Vector2
import engine.Component
import lib.instance_manager.Ref
import scala.compiletime.ops.boolean
import engine.render.renderer.render_element.RenderElement
import engine.math.Matrix3
import lib.emitter.*

class Entity protected (using val world: World) {

  // Givens
  final given World = world

  final private var _ref: Ref[Entity, Entity] = _

  def ref: Ref[Entity, Entity] = _ref

  private final val onReadyController = EControl[Unit]()
  final val onReady = onReadyController.emitter
  private final val onAddChildController = EControl[(Entity, Int)]()
  final val onAddChild = onAddChildController.emitter
  private final val onRemoveChildController = EControl[(Entity, Int)]()
  final val onRemoveChild = onRemoveChildController.emitter
  private final val onAddRenderElementController = EControl[RenderElement]()
  final val onAddRenderElement = onAddRenderElementController.emitter
  private final val onRemoveRenderElementController = EControl[RenderElement]()
  final val onRemoveRenderElement = onRemoveRenderElementController.emitter
  private final val onParentChangedController = EControl[Option[Entity]]()
  final val onParentChanged = onParentChangedController.emitter
  private final val onDestroyQueuedController = EControl[Unit]()
  final val onDestroyQueued = onDestroyQueuedController.emitter
  private final val onDestroyedController = EControl[Unit]()
  final val onDestroyed = onDestroyedController.emitter

  final private var _ready: Boolean = false
  final private var _name: String = "Unnamed Entity"
  final private var _localPosition: Vector2 = Vector2.zero
  final private var _localRotation: Float = 0
  final private var _localScale: Vector2 = Vector2.one
  final private var _parent: Option[Entity] = None
  final private var _children: List[Entity] = Nil
  final private var _cancelDestroy: Boolean = false

  final private var _renderElements: List[RenderElement] = Nil

  // Accessors

  final private def ready: Boolean = _ready
  final def renderElements: List[RenderElement] = _renderElements
  final def addRenderElement(renderElement: RenderElement): Unit =
    _renderElements = _renderElements :+ renderElement
    onAddRenderElementController.emit(renderElement)
  final def removeRenderElement(renderElement: RenderElement): Unit =
    _renderElements = _renderElements.filterNot(_ == renderElement)
    onRemoveRenderElementController.emit(renderElement)

  /** Returns all children recursively with a depth-first search.
    */
  final def descendants: List[Entity] =
    def f(e: Entity): List[Entity] = e.children.flatMap(f)
    f(this)

  final def name: String = _name
  def name_=(value: String): Unit =
    _name = value

  final def localPosition: Vector2 = _localPosition

  final def localPosition_=(value: Vector2): Unit =
    _localPosition = value

  final def globalPosition: Vector2 =
    parent.match
      case None    => _localPosition
      case Some(p) => p.globalPosition + localPosition

  final def globalPosition_=(value: Vector2): Unit =
    parent.match
      case None    => _localPosition = value
      case Some(p) => _localPosition = value - p.globalPosition

  final def localRotation: Float = _localRotation

  final def localRotation_=(value: Float): Unit =
    _localRotation = value

  final def globalRotation: Float =
    parent.match
      case None    => _localRotation
      case Some(p) => p.globalRotation + _localRotation

  final def globalRotation_=(value: Float): Unit =
    parent.match
      case None    => _localRotation = value
      case Some(p) => _localRotation = value - p.globalRotation

  final def localScale: Vector2 = _localScale

  final def localScale_=(value: Vector2): Unit =
    _localScale = value

  final def globalScale: Vector2 =
    parent.match
      case None => _localScale
      case Some(p) =>
        Vector2(
          p.globalScale.x * _localScale.x,
          p.globalScale.y * _localScale.y
        )

  final def globalScale_=(value: Vector2): Unit =
    parent.match
      case None => _localScale = value
      case Some(p) =>
        _localScale =
          Vector2(value.x / p.globalScale.x, value.y / p.globalScale.y)

  final def localTransform: Matrix3 =
    Matrix3.transform(
      localPosition,
      localRotation,
      localScale
    )

  final def globalTransform: Matrix3 =
    Matrix3.transform(
      globalPosition,
      globalRotation,
      globalScale
    )

  def parent: Option[Entity] = _parent
  // final def parent_=(value: Entity): Unit =
  //   Option(value) match
  //     case None =>
  //       throw new IllegalArgumentException(
  //         "Cannot set `parent` to null value. If you want to remove this Entity from its parent, call `removeChild` on the parent instead."
  //       )
  //     case Some(value) =>
  //       if (value == parent)
  //         throw new IllegalArgumentException(s"Already have $value as parent.")
  //       else
  //         _parent = Option(value)
  //         onParentChangedController.emit(Option(value))

  def children: List[Entity] = _children

  final def addChild(child: Entity, index: Int = -1): Unit =
    require(child != this, "Cannot add an entity to itself as a child.")
    val n = if index < -1 then _children.size - index + 1 else index
    _children = _children.take(n) ::: List(child) ::: _children.drop(n)
    child._parent = Some(this)
    child.onParentChangedController.emit(child.parent)
    onAddChildController.emit((child, n))

  final def removeChild(child: Entity): Unit =
    val n = _children.indexOf(child)
    _children = _children.filterNot(_ == child)
    onRemoveChildController.emit((child, n))

  final def removeChildAt(index: Int): Unit =
    val c = _children(index)
    removeChild(c)

  final def destroy(): Unit = {
    // Emit pre-destroy event
    _cancelDestroy = false
    onDestroyQueuedController.emit()

    // Cancel destroy if `cancelDestroy()` has been called
    if (_cancelDestroy)
      _cancelDestroy = false
      return

    // Destroy all children
    _children.foreach(_.destroy())
    // Remove from parent
    _parent.map(p => p.removeChild(this))
    // Remove from world
    world.destroy(this)
    // Emit destroyed event
    onDestroyedController.emit()
  }

  final def cancelDestroy(): Unit =
    // require()
    _cancelDestroy = true

  final def makeReady() = {
    _ref = world._entityManager.register(this)
    onReadyController.emit()
  }

  // // Generic Overrides
  // override def toString(): String =
  //   s"Entity(name: $name, position: ${position.formatted(3)}, children: ${children.size})"
}

object Entity {
  def apply(name: String = "Unnamed Entity")(using world: World): Entity =
    var entity = new Entity()
    entity.name = name
    entity.makeReady()
    entity
}
