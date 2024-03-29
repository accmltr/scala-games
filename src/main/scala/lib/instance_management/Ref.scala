package lib.instance_management

final class Ref[T] private[instance_management] (
    val id: Int,
    val manager: InstanceManager[T]
) {
  def instance: Option[T] = manager.instance(id)
  def destroy(): Unit = manager.destroy(id)

  // def flatMap
}

object Ref {
  def unapply[T](ref: Ref[T]): Option[T] = ref.instance
  // implicit def rti[T](ref: Ref[T]): T = ref.inst
}

case class Person(name: String)

object Test {
  val manager = InstanceManager[Person]()
  val peter = manager.register(Person("Peter"))

  def main(args: Array[String]): Unit = {
    printPersonRef(peter)
    printPersonRef(Ref[Person](2, manager))
  }

  def printPersonRef(r: Ref[Person]): Unit =
    r match
      case Ref(p) => println(p.name)
      case _      => println("No person found in Ref.")
}
