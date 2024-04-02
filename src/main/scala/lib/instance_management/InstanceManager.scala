package lib.instance_management

import scala.collection.immutable.Queue
import lib.event.Event
import scala.reflect.ClassTag

/** Manages instances of a certain type.
  *
  * **Purpose:** Prevent memory leaks and track instances.
  *
  * **Note:** This class was mainly created for a solution to the problem of
  * destroying game objects in a game engine, but it can be used in any
  * situation where references to objects should be kept safe and managed in one
  * place.
  */
class InstanceManager[T: ClassTag]() {

  val onRegister = Event[(T, Int)]
  val onDestroy = Event[(T, Int)]

  private var _refs: Map[Int, T] = Map.empty
  private var _nextRefNr: Int = 0

  /** Returns the total amount of objects that have been managed by this
    * `InstanceManager`.
    */
  def total: Int = _nextRefNr
  private def _newRefNr(): Int = this.synchronized {
    val id = _nextRefNr
    _nextRefNr += 1
    id
  }

  def instance[K <: T: ClassTag](refNr: Int): Option[K] =
    _refs.get(refNr) match
      case None => None
      case Some(value) =>
        value match
          case k: K => Some(k)
          case _: T =>
            throw Exception("Instance found, but was not of type K.")

  def managedRefNrs: List[Int] =
    _refs.map(_._1).toList

  /** **Note:** Be sure to get rid of local references to the newly registered
    * instance, and use only the returned `Ref` from there on out.
    *
    * @param t
    *   The object to be encapsulated.
    * @return
    */
  def register(t: T): Ref[T, T] = this.synchronized {
    require(t != null, "'t' may not be null")
    var nr = _newRefNr()
    _refs += nr -> t
    onRegister.emit((t, nr))
    Ref[T, T](nr, this)
  }

  def destroy(refNr: Int): Unit = {
    require(_refs.contains(refNr), "Trying to destroy non-existant instance.")
    this.synchronized {
      val instance = _refs(refNr)
      _refs -= refNr
      onDestroy.emit((instance, refNr))
    }
  }
}
