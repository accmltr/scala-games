package lib.instance_management

import scala.collection.immutable.Queue

/** Tracks instances of a certain type.
  *
  * **Purpose:** Prevent memory leaks and track instances.
  *
  * **Note:** This class was mainly created for a solution to the problem of
  * destroying game objects in a game engine.
  */
class InstanceManager[T]() {
  private var _refs: Map[Int, T] = Map.empty
  private var _nextRefNr: Int = 0
  private var _historySize: Int = 10000
  private var _destroyedIds: Queue[Int] = Queue.empty

  def historySize: Int = _historySize
  def historySize_=(value: Int): Unit =
    _historySize = value
    if (_destroyedIds.size > _historySize)
      _destroyedIds = _destroyedIds.drop(_destroyedIds.size - _historySize)

  def destroyedIds: Vector[Int] = _destroyedIds.toVector

  def totalRegistered: Int = _nextRefNr
  private def _newRefNr(): Int = this.synchronized {
    val id = _nextRefNr
    _nextRefNr += 1
    id
  }

  def instance(refNr: Int): Option[T] =
    _refs.get(refNr)

  def managedRefNrs: List[Int] =
    _refs.map(_._1).toList

  /** **Note:** Be sure to get rid of local references to the newly registered
    * instance, and use only the returned `Instance` from there on out.
    *
    * @param t
    *   The object to be encapsulated.
    * @return
    */
  def register(t: T): Ref[T] = this.synchronized {
    require(t != null, "'t' may not be null")
    var nr = _newRefNr()
    _refs += nr -> t
    Instance(nr, this)
  }

  private[instance_management] def destroy(refNr: Int): Unit =
    require(
      _refs.contains(refNr),
      "Trying to destroy non-existant Ref instance."
    )
    this.synchronized {
      _refs -= refNr
      if (_destroyedIds.size >= _historySize)
        _destroyedIds = _destroyedIds.dequeue._2
      _destroyedIds = _destroyedIds.enqueue(refNr)
    }
}
