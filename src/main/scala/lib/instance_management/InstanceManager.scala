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
  private var _instances: Map[Int, Ref[T]] = Map.empty
  private var _nextId: Int = 0
  private var _historySize: Int = 10000
  private var _destroyedIds: Queue[Int] = Queue.empty

  def instances: Vector[Ref[T]] = _instances.values.toVector
  def historySize: Int = _historySize
  def historySize_=(value: Int): Unit =
    _historySize = value
    if (_destroyedIds.size > _historySize)
      _destroyedIds = _destroyedIds.drop(_destroyedIds.size - _historySize)
  def destroyedIds: Vector[Int] = _destroyedIds.toVector

  def newId: Int = this.synchronized {
    val id = _nextId
    _nextId += 1
    id
  }

  /** **Note:** Be sure to get rid of local references to the newly registered
    * instance, and use only the returned `Instance` from there on out.
    *
    * @param rawInstance
    *   The instance to register and encapsulate within the `Instance` wrapper
    *   class.
    * @return
    */
  def register(rawInstance: T): Ref[T] = this.synchronized {
    require(rawInstance != null, "'rawInstance' parameter may not be null")
    val ref = Ref(Option(rawInstance), newId, this)
    _instances += ref.id -> ref
    ref
  }

  private[instance_management] def _onDestroyed(instance: Ref[T]): Unit =
    this.synchronized {
      _instances -= instance.id
      if (_destroyedIds.size >= _historySize)
        _destroyedIds = _destroyedIds.dequeue._2
      _destroyedIds = _destroyedIds.enqueue(instance.id)
    }

}
