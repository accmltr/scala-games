package lib.instance_manager

import lib.emitter.*

/** Manages instances of a certain type.
  *
  * **Purpose:** Prevent memory leaks and track instances.
  *
  * **Note:** This class was mainly created for a solution to the problem of
  * destroying game objects in a game engine, but it can be used in any
  * situation where references to objects should be kept safe and managed in one
  * place.
  */
final class InstanceManager[T] {

  private val onRegisterController = EControl[Ref[T, T]]()
  val onRegister: Emitter[Ref[T, T]] = onRegisterController.emitter
  private val onDestroyingController = EControl[Ref[T, T]]()
  val onDestroying: Emitter[Ref[T, T]] = onDestroyingController.emitter
  private val onDestroyController = EControl[Ref[T, T]]()
  val onDestroy: Emitter[Ref[T, T]] = onDestroyController.emitter

  private var _refs: List[Ref[T, T]] = Nil
  def refs: List[Ref[T, T]] = _refs

  /** **Note:** Be sure to get rid of local references to the newly registered
    * instance, and use only the returned `Ref` from there on out.
    *
    * @param instance
    *   The object to be encapsulated.
    */
  def register[K <: T](instance: K): Ref[K, T] = this.synchronized {
    Option(instance) match
      case None =>
        throw new IllegalArgumentException("Arg 'instance' may not be null.")
      case Some(value) =>
        val r = Ref[K, T](value, this)
        _refs = r :: _refs
        onRegisterController.emit(r)
        r
  }

  def destroy[K <: T](ref: Ref[K, T]): Unit = {
    this.synchronized {
      val updated = _refs.filterNot(_.get == ref.get)
      require(_refs != updated, "Trying to destroy non-existant instance.")
      onDestroyingController.emit(ref)
      _refs = updated
      onDestroyController.emit(ref)
    }
  }
}
