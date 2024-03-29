package engine.core

import scala.reflect.ClassTag
import engine.math.Vector2
import engine.Component
import lib.instance_management.Instance

class Node private[engine] (game: Game) {

  // Engine Node Management
  val instance = game.register(this)

  // Givens
  given Game = game

  var name: String = "unnamed"
  var position: Vector2 = Vector2.zero
  var rotation: Float = 0
  var scale: Vector2 = Vector2.one
  var parent: Option[Instance[Node]] = None
  var children: List[Instance[Node]] = List.empty
  var components: List[Component] = List.empty

  // Generic Overrides
  override def toString(): String =
    s"Node(name: $name, position: ${position.formatted(3)}, children: ${children.size}, " +
      s"components: ${components.size})"
}

object Node {
  def apply()(using game: Game): Instance[Node] = (new Node(game)).instance
}
