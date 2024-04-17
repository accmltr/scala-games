package lib.property

final case class PropertyController[T](initialValue: T) {

  val property: Property[T] = new Property[T](initialValue)

  def setter: T => T = property.setter

  def setter_=(s: T => T): Unit = property.setter = s

  def getter: T => T = property.getter

  def getter_=(g: T => T): Unit = property.getter = g

  def value: T = property.value

  def value_=(v: T): Unit = property.value = v
}

object PropertyController {
  def apply[T](initialValue: T, setter: T => T = (t: T) => t, getter: T => T = (t: T) => t): PropertyController[T] =
    val controller = new PropertyController(initialValue)
    controller.setter = setter
    controller.getter = getter
    controller
}