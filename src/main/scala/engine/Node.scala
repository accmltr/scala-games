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

  override def toString(): String =
    s"Node(name: $name, position: ${position.formatted(3)}, children: ${children.size}, " +
      s"components: ${components.size})"
}

// /** The internal representation of a Node.
//   */
// private[engine] final case class _Node(
//     _name: String = "unnamed",
//     _position: Vector2 = Vector2.zero,
//     _rotation: Float = 0,
//     _scale: Vector2 = Vector2.one,
//     _parent: Option[Node] = None,
//     _children: List[Node] = List.empty,
//     _components: List[Component] = List.empty,
//     _game: Game = null
// ) {

//   override def toString(): String =
//     s"Node(name: $_name, position: ${_position.formatted(3)}, children: ${_children.size}, " +
//       s"components: ${_components.size})"
// }

// object Node {
//   def apply(
//       name: String = "unnamed",
//       position: Vector2 = Vector2.zero,
//       rotation: Float = 0,
//       scale: Vector2 = Vector2.one,
//       parent: Option[Node] = None,
//       children: List[Node] = List.empty,
//       components: List[Component] = List.empty
//   ): Node = {
//     val node = new Node()
//     node._node =
//       _Node(name, position, rotation, scale, parent, children, components)
//     node
//   }
// }

// /** The external interface for a _Node.
//   */
// final class Node private {
//   private var _node: _Node = null

//   // Methods
//   private[engine] def game_=(_game: Game): Unit =
//     _node = _node.copy(_game = _game)
//   def destroy(): Unit =
//     _node = null

//   // Getters
//   def game: Game = _node._game
//   def name: String = _node._name
//   def position: Vector2 = _node._position
//   def rotation: Float = _node._rotation
//   def scale: Vector2 = _node._scale
//   def parent: Option[Node] = _node._parent
//   def children: List[Node] = _node._children
//   def components: List[Component] = _node._components

//   // Setters
//   def name_=(_name: String): Unit =
//     _node = _node.copy(_name = _name)
//   def position_=(_position: Vector2): Unit =
//     _node = _node.copy(_position = _position)
//   def rotation_=(_rotation: Float): Unit =
//     _node = _node.copy(_rotation = _rotation)
//   def scale_=(_scale: Vector2): Unit =
//     _node = _node.copy(_scale = _scale)
//   def parent_=(_parent: Option[Node]): Unit =
//     if (game != null && _parent.isDefined)
//       throw new IllegalArgumentException(
//         "Cannot set parent of the root node"
//       )
//     _node = _node.copy(_parent = _parent)
//   def child_=(_child: Node): Unit =
//     _node = _node.copy(_children = _child :: _node._children)
//   def removeChild(_child: Node): Unit =
//     _node = _node.copy(_children = _node._children.filterNot(_ == _child))
//   def addComponent(_component: Component): Unit =
//     _node = _node.copy(_components = _component :: _node._components)
//   def removeComponent(_component: Component): Unit =
//     _node = _node.copy(
//       _components = _node._components.filterNot(_ == _component)
//     )
//   def hasComponent[T <: Component: ClassTag](implicit
//       cls: ClassTag[T]
//   ): Boolean =
//     _node._components.exists(cls.runtimeClass.isInstance(_))
//   def getComponent[T <: Component: ClassTag](implicit
//       cls: ClassTag[T]
//   ): Option[T] =
//     _node._components
//       .find(cls.runtimeClass.isInstance(_))
//       .map(_.asInstanceOf[T])
//   def getComponents[T <: Component: ClassTag](implicit
//       cls: ClassTag[T]
//   ): List[T] =
//     _node._components
//       .filter(cls.runtimeClass.isInstance(_))
//       .map(_.asInstanceOf[T])

//   // Generic Overrides
//   override def toString(): String = _node.toString()
//   override def equals(other: Any): Boolean = _node.equals(other)
//   override def hashCode(): Int = _node.hashCode()

// }
