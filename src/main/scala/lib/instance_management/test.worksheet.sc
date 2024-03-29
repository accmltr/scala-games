import scala.collection.immutable.Queue
import lib.instance_management.InstanceManager
case class Person(name: String, age: Int)

var instanceManager = new InstanceManager[Person]()
var instance = instanceManager.register(Person("John", 30))

instance.name
instance.age

val queue: Queue[Int] = Queue(1, 2, 3, 4, 5)
queue.dequeue
queue.enqueue(6)
queue.drop(3)
