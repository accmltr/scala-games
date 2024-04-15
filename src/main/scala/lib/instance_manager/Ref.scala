package lib.instance_manager

import scala.language.implicitConversions

/** The `Ref` class provides a safe mechanism for accessing objects that can
 * be destroyed by its `InstanceManager`. The idea is that only the
 * `InstanceManager` object may contain the original references to the objects
 * that it manages, making it possible to free any object at any time by
 * erasing its original reference and having the garbage-collector free that
 * object from system memory.
 *
 * IMPORTANT:
 * Never store the result of `Ref.get` in a variable or closure directly.
 * Always store the `Ref` object, and only access `Ref.get` when you plan to
 * do a transformation on, or analysis of, the reference it contains.
 */
final class Ref[+K <: T, T] private(val manager: InstanceManager[T]) {

  private var instance: Option[T] = None

  /** Use this method to access the managed object stored inside the
   * `InstanceManager` of this `Ref`. It will return an `Option` containing
   * either nothing, or the instance you are looking for.
   *
   * There are a few common patterns to remember when trying to access this
   * value.
   *
   * ### 1. Pattern Matching
   * {{{
   * // Assume you have a Ref to a Player instance in your game...
   * val playerRef = Ref(Player("Frank"))
   * // Let's say you want to damage that player and print their name:
   * playerRef.get match
   *   case Some(player: Player) =>
   *     player.damage(10)
   *     println(s"Hit player ${player.userName} for 10 damage.")
   *   case None =>
   *     println("Tried damaging a player that does not exist anymore!")
   * }}}
   *
   * ### 2. `map` Method
   * {{{
   * val playerRef = Ref(Player("Frank"))
   * playerRef.get.map(player =>
   *   // This code only runs if the player exists.
   *   player.damage(10)
   *   println(s"Hit player ${player.userName} for 10 damage.")
   * )
   * }}}
   *
   * ### 3. For-comprehensions
   * {{{
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
   * }}}
   *
   * @return
   */
  def get: Option[K] =
    instance.map(_.asInstanceOf[K])

  /** Note: This will only help to encourage the garbage collector to remove the
   * object from system memory if the object is not being stored in a local
   * variable or closure somewhere else in code. The object will still be
   * removed from the `InstanceManager` and will not be accessible from there
   * or from any related `Ref` anymore.
   */
  private[instance_manager] def clear(): Unit =
    instance = None
}

object Ref {

  private[instance_manager] def apply[K <: T, T](
                                                  i: K,
                                                  m: InstanceManager[T]
                                                ): Ref[K, T] =
    val ref = new Ref[K, T](m)
    ref.instance = Option(i)
    ref

  implicit def toGet[K <: T, T](ref: Ref[K, T]): Option[K] = ref.get

  def unapply[K <: T, T](ref: Ref[K, T]): Option[(K, InstanceManager[T])] =
    Some((ref.instance.get.asInstanceOf[K], ref.manager))
}
