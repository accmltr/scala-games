package lib.instance_management

sealed trait Ref[T] private[instance_management] (
) {
  def map(f: T => Unit): Ref[T]
  def flatMap(f: T => Unit): Ref[T]
  def foreach(f: T => Unit): Unit
}

case class Instance[T](val id: Int, val manager: InstanceManager[T])
    extends Ref[T] {
  def get: Option[T] = manager.instance(id)
  def destroy(): Unit = manager.destroy(id)

  def map(f: T => Unit): Ref[T] =
    get.foreach(f)
    this

  def flatMap(f: T => Unit): Ref[T] =
    get.foreach(f)
    this

  def foreach(f: T => Unit): Unit =
    get.foreach(f)

  override def toString(): String =
    s"Instance($id)"
}

case object Empty extends Ref[Nothing] {
  def map(f: Nothing => Unit): Ref[Nothing] = this
  def flatMap(f: Nothing => Unit): Ref[Nothing] = this
  def foreach(f: Nothing => Unit): Unit = ()
}

// object Ref {
//   def unapply[T](ref: Ref[T]): Option[T] = ref.get
//   // implicit def rti[T](ref: Ref[T]): T = ref.inst
// }

case class Person(name: String, var health: Int = 10) {
  def damage(amount: Int) =
    health -= amount

  override def toString(): String =
    s"Person($name, $health)"
}

object Test {
  def main(args: Array[String]): Unit =
    val m = InstanceManager[Person]()
    val p = m.register(Person("Pizza", 13))

    p.flatMap(println)

    for i <- p
    do println(i)
}
