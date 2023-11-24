import lib.instance_management.InstanceManager
case class Person(name: String, age: Int)

var instanceManager = new InstanceManager[Person]()
var instance = instanceManager.register(Person("John", 30))

instance.name
instance.age
