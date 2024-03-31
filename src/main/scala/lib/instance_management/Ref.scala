package lib.instance_management

/** The `Ref[T]` class provides a safe mechanism for accessing objects that can
  * be destroyed by its `InstanceManager`. The idea is that only the
  * `InstanceManager` object may contain the original references to the objects
  * that it manages, making it possible to free any object at any time by
  * erasing its original reference and having the garbage-collector free that
  * object from system memory. Externally, these objects should only be stored
  * in fields or closures using the Ref class!
  *
  * @param number
  *   Represents the total number of instances managed by this `InstanceManager`
  *   after this instance was added to it. Also used as the ID for the instance
  *   by the `InstanceManager`.
  * @param manager
  */
final case class Ref[T](val number: Int, val manager: InstanceManager[T]) {

  /** Use this method to access the managed object stored inside this `Ref`'s
    * `InstanceManager`. It will return an `Option` containing either nothing,
    * or the instance you are looking for. actual reference
    *
    * @return
    */
  def get: Option[T] = manager.instance(number)
  def destroy(): Unit = manager.destroy(number)

  override def toString(): String =
    s"Instance($number)"
}

object Ref {
  implicit def toGet[T](ref: Ref[T]): Option[T] = ref.get
}

// case class Person(name: String, var health: Int = 10) {
//   def damage(amount: Int) =
//     health -= amount

//   override def toString(): String =
//     s"Person(\"$name\", $health)"
// }

// object Test {
//   def main(args: Array[String]): Unit =
//     val m = InstanceManager[Person]()
//     val p = m.register(Person("Yeet", 13))

//     // p.destroy()

//     p.get match
//       case None        => println("Person doesn't exist anymore!")
//       case Some(value) => println(value)

// }
