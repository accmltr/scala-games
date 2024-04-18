package lib.property_tracker

final case class PropertyController[T](initialValue: T) {

  val property: Property[T] = new Property[T](initialValue)

  def setter: T => T = property.setter

  /** Use with caution, this overrides the current setter method for `property`. */
  def setter_=(s: T => T): Unit = property.setter = s

  def getter: T => T = property.getter

  /** Use with caution, this overrides the current getter method for `property`. */
  def getter_=(g: T => T): Unit = property.getter = g

  def value: T = property.value

  def value_=(v: T): Unit = property.value = v
}

object PropertyController {
  /**
   *
   * @param setter
   * You can optionally provide a function that is used to process any new
   * values before returning them to be set. For example, here is a setter
   * function that makes sure the `hp` of some character in a game cannot be
   * set to a value lower than 0, or higher than the character max hp:
   * {{{
   *   val maxHp = 100
   *   val hpSetter = (newHp: Int) =>
   *     if newHp < 0
   *     then 0
   *     else if newHp > maxHp
   *     then maxHp
   *     else newHp
   *
   *   val hp = Property(maxHp, hpSetter)
   *  * }}}
   */
  def apply[T](initialValue: T, setter: T => T = (t: T) => t, getter: T => T = (t: T) => t): PropertyController[T] =
    val controller = new PropertyController(initialValue)
    controller.setter = setter
    controller.getter = getter
    controller
}