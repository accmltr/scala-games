package lib.instance_management

/** @param number
  *   Represents the total number of instances managed by this InstanceManager
  *   after this instance was added to it. Also used as the ID for the instance
  *   by the InstanceManager.
  * @param manager
  */
final case class Ref[T](val number: Int, val manager: InstanceManager[T]) {
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
