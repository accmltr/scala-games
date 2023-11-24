package lib.instance_management

import scala.collection.immutable.Queue

/** **Purpose:** Prevent memory leaks and track instances.
  */
class InstanceManager[T](var historySize: Int = 10000) {

  private var _instances: Map[Int, Instance[T]] = Map.empty
  private var _nextId: Int = 0
  private var _destroyedIds: Queue[Int] = Queue.empty

  def instances: Vector[Instance[T]] = _instances.values.toVector
  def destroyedIds: Vector[Int] = _destroyedIds.toVector

  def newId: Int = this.synchronized {
    val id = _nextId
    _nextId += 1
    id
  }

  def register(i: T): Instance[T] = this.synchronized {
    require(i != null, "'instance' parameter may not be null")
    val ref = Instance(Option(i), newId, this)
    _instances += ref.id -> ref
    ref
  }

  private[instance_management] def _onDestroyed(instance: Instance[T]): Unit =
    this.synchronized {
      _instances -= instance.id
      if (_destroyedIds.size >= historySize)
        _destroyedIds = _destroyedIds.dequeue._2
      _destroyedIds = _destroyedIds.enqueue(instance.id)
    }

}
