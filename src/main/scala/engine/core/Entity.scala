package engine.core

import scala.reflect.ClassTag
import engine.math.Vector2
import engine.Component
import lib.instance_management.Ref

class Entity private[engine] (world: World) {

  // Engine Node Management
  val instance = world.register(this)

  // Givens
  given World = world

  var name: String = "entity"
  var position: Vector2 = Vector2.zero
  var rotation: Float = 0
  var scale: Vector2 = Vector2.one
  var parent: Option[Ref[Entity]] = None
  var children: List[Ref[Entity]] = List.empty
  var components: List[Component] = List.empty

  // Generic Overrides
  override def toString(): String =
    s"Node(name: $name, position: ${position.formatted(3)}, children: ${children.size}, " +
      s"components: ${components.size})"
}

object Entity {
  def apply(name: String = "entity")(using
      world: World
  ): Ref[Entity] =
    var instance = (new Entity(world)).instance
    // instance.name = name
    instance
}
