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

case class Person(name: String, var health: Int = 10) {
  def damage(amount: Int) =
    health -= amount
}

object Test {
  val manager = InstanceManager[Person]()
  val peter = manager.register(Person("Peter", 77))

  def main(args: Array[String]): Unit = {
    printPersonRef(peter)
    printPersonRef(Ref[Person](2, manager))
    peter.match
      case Ref(p) => p.damage(3)

    printPersonRef(peter)
  }

  def damage(personRef: Ref[Person], amount: Int) =
    personRef match
      case Ref(person) => person.health -= amount

  def printPersonRef(r: Ref[Person]): Unit =
    r match
      case Ref(p) => println(s"${p.name} has ${p.health} health.")
      case _      => println("No person found in Ref.")
}
