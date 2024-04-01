package engine.core

import scala.reflect.ClassTag
import engine.math.Vector2
import engine.Component
import lib.instance_management.Ref

class Entity private[engine] (world: World) {

  // Engine Node Management
  val ref = world.entityManager.register(this)

  // Givens
  given World = world

  val onAddChild = Event[(Entity, Int)]
  val onRemoveChild = Event[(Entity, Int)]
  val onParentChanged = Event[Entity]
  val onPreDestroy = Event[Unit]

  private var _name: String = "Unnamed Entity"
  private var _position: Vector2 = Vector2.zero
  private var _rotation: Float = 0
  private var _scale: Vector2 = Vector2.one
  private var _parent: Option[Entity] = None
  private var _children: List[Entity] = List.empty

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
    // Destroy all children
    _children.foreach(_.destroy())
    // Remove from parent
    _parent.map(_.removeChild(this))
    // Remove from world

  }

  // Generic Overrides
  override def toString(): String =
    s"Node(name: $name, position: ${position.formatted(3)}, children: ${children.size})"
}

object Entity {
  def apply(name: String = "Unnamed Entity")(using
      world: World
  ): Ref[Entity] =
    var entity = (new Entity(world))
    entity.name = name
    entity.ref
}
