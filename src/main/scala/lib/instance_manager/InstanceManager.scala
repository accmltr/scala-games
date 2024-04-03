package lib.instance_manager

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
class InstanceManager[T]() {

  val onRegister = Event[Ref[T]]
  val onDestroying = Event[Ref[T]]
  val onDestroy = Event[Ref[T]]

  private var _refs: List[Ref[T]] = Nil

  /** **Note:** Be sure to get rid of local references to the newly registered
    * instance, and use only the returned `Ref` from there on out.
    *
    * @param t
    *   The object to be encapsulated.
    * @return
    */
  def register[K <: T](instance: K): Ref[K] = this.synchronized {
    Option(instance) match
      case None =>
        throw new IllegalArgumentException("Arg 'instance' may not be null.")
      case Some(value) =>
        val r = Ref(value)
        _refs = r :: _refs
        r
  }

  def destroy[K <: T](ref: Ref[K]): Unit = {
    this.synchronized {
      val updated = _refs.filterNot(_.get == ref.get)
      require(_refs != updated, "Trying to destroy non-existant instance.")
      _refs = updated
      onDestroy.emit(ref)
    }
  }
}
