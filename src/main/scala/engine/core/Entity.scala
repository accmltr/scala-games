package engine.core

import scala.reflect.ClassTag
import engine.math.Vector2
import engine.Component
import lib.instance_manager.Ref
import scala.compiletime.ops.boolean
import lib.event.Event

class Entity protected (using val world: World) {

  // Givens
  given World = world

  val ref: Ref[Entity, Entity] = world._entityManager.register(this)

  val onReady = Event[Unit]
  val onAddChild = Event[(Entity, Int)]
  val onRemoveChild = Event[(Entity, Int)]
  val onParentChanged = Event[Entity]
  val onDestroyQueued = Event[Unit]
  val onDestroyed = Event[Unit]

  private var _ready: Boolean = false
  private var _name: String = "Unnamed Entity"
  private var _position: Vector2 = Vector2.zero
  private var _rotation: Float = 0
  private var _scale: Vector2 = Vector2.one
  private var _parent: Option[Entity] = None
  private var _children: List[Entity] = List.empty
  private var _cancelDestroy: Boolean = false

  def ready: Boolean = _ready

  def name: String = _name
  def name_=(value: String): Unit =
    _name = value

  def position: Vector2 = _position
  def position_=(value: Vector2): Unit =
    _position = value

  def rotation: Float = _rotation
  def rotation_=(value: Float): Unit =
    _rotation = value

  def scale: Vector2 = _scale
  def scale_=(value: Vector2): Unit =
    _scale = value

  def parent: Option[Entity] = _parent
  def parent_=(value: Entity): Unit =
    Option(value) match
      case None =>
        throw new IllegalArgumentException(
          "Cannot set `parent` to null value. If you want to remove this Entity from its parent, call `removeChild` on the parent instead."
        )
      case Some(value) =>
        if (value == parent)
          throw new IllegalArgumentException(s"Already have $value as parent.")
        else
          _parent = Option(value)
          onParentChanged.emit(value)

  def children: List[Entity] = _children

  def addChild(child: Entity, index: Int = -1): Unit =
    val n = if index < -1 then _children.size - index + 1 else index
    _children = _children.take(n) ::: List(child) ::: _children.drop(n)
    onAddChild.emit((child, n))

  def removeChild(child: Entity): Unit =
    val n = _children.indexOf(child)
    _children = _children.filterNot(_ == child)
    onRemoveChild.emit((child, n))

  def removeChildAt(index: Int): Unit =
    val c = _children(index)
    removeChild(c)

  def destroy(): Unit = {
    // Emit pre-destroy event
    _cancelDestroy = false
    onDestroyQueued.emit()

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
    onDestroyed.emit()
  }

  def cancelDestroy(): Unit =
    _cancelDestroy = true

  def makeReady() = {
    onReady.emit()
  }

  // Generic Overrides
  override def toString(): String =
    s"Entity(name: $name, position: ${position.formatted(3)}, children: ${children.size})"
}

object Entity {
  def apply(name: String = "Unnamed Entity")(using world: World): Entity =
    var entity = new Entity()
    entity.name = name
    entity.makeReady()
    entity
}
