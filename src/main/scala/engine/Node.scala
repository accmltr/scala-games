package engine

import scala.reflect.ClassTag
import engine.math.Vector2
import engine.Component

class Node()(using game: Game) {

  // Givens
  given Game = game

  var name: String = "unnamed"
  var position: Vector2 = Vector2.zero
  var rotation: Float = 0
  var scale: Vector2 = Vector2.one
  var parent: Option[Node] = None
  var children: List[Node] = List.empty
  var components: List[Component] = List.empty

  // Generic Overrides
  override def toString(): String =
    s"Node(name: $name, position: ${position.formatted(3)}, children: ${children.size}, " +
      s"components: ${components.size})"
}
