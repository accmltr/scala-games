package lib.instance_management

import scala.collection.mutable.Queue

/** **Purpose:** Prevent memory leaks and track instances.
  */
class InstanceManager[T](var historySize: Int = 10000) {

  var instances: Map[Int, Instance[T]] = Map.empty

  private var nextId: Int = 0
  private var destroyedIds: Queue[Int] = Queue.empty

  def newId: Int = this.synchronized {
    val id = nextId
    nextId += 1
    id
  }

  def register(i: T): Instance[T] = this.synchronized {
    require(i != null, "'instance' parameter may not be null")
    val ref = Instance(Option(i), newId, this)
    instances += ref.id -> ref
    ref
  }

  private[instance_management] def onDestroyed(instance: Instance[T]): Unit =
    this.synchronized {
      instances -= instance.id
      if (destroyedIds.size >= historySize)
        destroyedIds.dequeue()
      destroyedIds.enqueue(instance.id)
    }

}
