package lib.instance_management

import scala.reflect.ClassTag

/** The `Ref[T]` class provides a safe mechanism for accessing objects that can
  * be destroyed by its `InstanceManager`. The idea is that only the
  * `InstanceManager` object may contain the original references to the objects
  * that it manages, making it possible to free any object at any time by
  * erasing its original reference and having the garbage-collector free that
  * object from system memory. Externally, these objects should only be stored
  * in fields or closures using the Ref class!
  *
  * @param number
  *   Represents the total number of instances managed by this `InstanceManager`
  *   after this instance was added to it. Also used as the ID for the instance
  *   by the `InstanceManager`.
  * @param manager
  */
final case class Ref[K: ClassTag, T >: K](
    val number: Int,
    val manager: InstanceManager[T]
) {

  /** Use this method to access the managed object stored inside the
    * `InstanceManager` of this `Ref`. It will return an `Option` containing
    * either nothing, or the instance you are looking for.
    *
    * There are a few common patterns to remember when trying to access this
    * value.
    *
    * ### 1. Pattern Matching
    * ```
    * // Assume you have a Ref to a Player instance in your game...
    * val playerRef = Ref(Player("Frank"))
    * // Let's say you want to damage that player and print their name:
    * playerRef.get match
    *   case Some(player: Player) =>
    *     player.damage(10)
    *     println(s"Hit player ${player.userName} for 10 damage.")
    *   case None =>
    *     println("Tried damaging a player that does not exist anymore!")
    * ```
    *
    * ### 2. `map` Method
    * ```
    * val playerRef = Ref(Player("Frank"))
    * playerRef.get.map(player =>
    *   // This code only runs if the player exists.
    *   player.damage(10)
    *   println(s"Hit player ${player.userName} for 10 damage.")
    * )
    * ```
    *
    * ### 3. For-comprehensions
    * ```
    * val playerRef = Ref(Player("Frank"))
    * for player <- playerRef.get
    * do
    *   // Similar to the map-method, this code only runs if the player exists.
    *   player.damage(10)
    *   println(s"Hit player ${player.userName} for 10 damage.")
    *
    * // For-comprehensions are particularly useful when working with multiple Options.
    * val playerRefNo2 = Ref(Player("Joe"))
    * for
    *   player1 <- playerRef.get
    *   player2 <- playerRefNo2.get
    * do
    *   println(s"${player1.userName} is friends with ${player2.userName}.")
    * ```
    *
    * @return
    */
  def get: Option[K] = manager.instance(number)

  // /** Tells the `InstanceManager` for this `Ref` to destroy the object that this
  //   * `Ref` points to.
  //   *
  //   * Note: This will only help to encourage the garbage collector to remove the
  //   * object from system memory if the object is not being stored in a local
  //   * variable or closure somewhere else in code. The object will still be
  //   * removed from the `InstanceManager` and will not be accessible from there
  //   * or from any related `Ref` anymore.
  //   */
  // def destroy(): Unit = manager.destroy(number)

  override def toString(): String =
    s"Instance($number)"
}

object Ref {
  implicit def toGet[K, T >: K](ref: Ref[K, T]): Option[K] = ref.get
}

// case class Person(name: String, var health: Int = 10) {
//   def damage(amount: Int) =
//     health -= amount

//   override def toString(): String =
//     s"Person(\"$name\", $health)"
// }

// object Test {
//   def main(args: Array[String]): Unit =
//     val m = InstanceManager[Person]()
//     val p = m.register(Person("Yeet", 13))

//     // p.destroy()

//     p.map(value => value.health)

//     p.get match
//       case None        => println("Person doesn't exist anymore!")
//       case Some(value) => println(value)

// }
