package lib.property

import engine.math.normalAngle
import lib.emitter.*

/** Intended to be used with immutable values where possible!
 * E.g. `latestCombatInteraction: Property[CombatInteraction]`,
 * where `CombatInteraction` is immutable.
 *
 * Sometimes mutable data structures are fine. For example,
 * `currentTarget: Property[Character]`, where the Character
 * class has many mutable properties, such as `hp`, `position` etc.
 */
final class Property[T] private[property](initialValue: T) {

  private[property] var setter: T => T = (t: T) => t
  private[property] var getter: T => T = (t: T) => t



  private val _onSetController = EmitterController[(T, T)]()
  private val _onChangeController = EmitterController[(T, T)]()

  /** Emitted whenever the value of this property is set — even when the new
   * value is the same as the current value.
   *
   * Emit data contains a tuple with the previous and new values.
   */
  val onSet: Emitter[(T, T)] = _onSetController.emitter

  /** Emitted whenever the value of this property is set to a new value that is
   * different from the current one.
   *
   * Emit data contains a tuple with the previous and new values.
   */
  val onChange: Emitter[(T, T)] = _onChangeController.emitter

  private var _value: T = initialValue

  def value: T = getter(_value)

  private[property] def value_=(v: T): Unit =
    val previous = _value
    _value = setter(v)
    _onSetController.emit((previous, _value))
    if (previous != _value)
      _onChangeController.emit((previous, _value))
}

