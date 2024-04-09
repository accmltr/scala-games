package lib.property

import lib.emitter.*

/** Intended to be used with immutable values where possible!
  *
  * Sometimes mutable data structures are fine. For example, `currentTarget:
  * Property[Character]`, where the Character class has many mutable properties,
  * such as `hp`, `position` etc.
  *
  * @param setter
  *   You can optionally provide a function that is used to process any new
  *   values before returning them to be set. For example, here is a setter
  *   function that makes sure the `hp` of some character in a game cannot be
  *   set to a value lower than 0, or higher than the character max hp:
  *   ```
  *   val maxHp = 100
  *   val hpSetter = (newHp: Int) =>
  *     if newHp < 0
  *     then 0
  *     else if newHp > maxHp
  *     then maxHp
  *     else newHp
  *
  *   val hp = Property(maxHp, hpSetter)
  *   ```
  */
final case class Property[T](initialValue: T, setter: T => T = (t: T) => t) {

  private val onSetController = EControl[(T, T)]()

  /** Emitted whenever the value of this property is set — even when the new
    * value is the same as the current value.
    *
    * Emit data contains a tuple with the previous and new values.
    */
  val onSet = onSetController.emitter

  private val onChangeController = EControl[(T, T)]()

  /** Emitted whenever the value of this property is set to a new value that is
    * different from the current one.
    *
    * Emit data contains a tuple with the previous and new values.
    */
  val onChange = onChangeController.emitter

  private var _value: T = initialValue

  def value: T = _value
  def value_=(v: T): Unit =
    val previous = _value
    _value = setter(v)
    onSetController.emit((previous, _value))
    if (previous != _value)
      onChangeController.emit((previous, _value))
}
