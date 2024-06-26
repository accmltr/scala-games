package engine

import org.joml

/** Some useful math features for making games.
  */
package object math {
  // ------------------------
  // constants
  // ------------------------
  inline val pi = 3.141592653589793f

  // ------------------------
  // operations
  // ------------------------

  def clamp(v: Float, min: Float, max: Float) = joml.Math.clamp(v, min, max)

  /** Makes sure angle is represented in positive form. Returns a number between
    * 0[inclusive] and 2*PI[non-inclusive], i.e. returns 2*PI radians as 0
    * radians.
    *
    * E.g:
    * {{{
    *  normalAngle(radians(-315)) == radians(45)
    *  normalAngle(raFdians(45)) == radians(45)
    *  normalAngle(radians(-90)) == radians(270)
    * }}}
    *
    * @param rad
    *   Angle in radians
    * @return
    */
  def normalAngle(rad: Float): Float = {
    val modded = rad % (2 * pi)
    if modded < 0
    then modded + (2 * pi)
    else modded
  }

  /** Checks whether two angles are equal within a certain threshold of error
    * `epsilon`.
    *
    * @param a1
    * @param a2
    * @param epsilon
    * @return
    */
  def anglesEqual(a1: Float, a2: Float, epsilon: Float = 0f): Boolean =
    angleInBounds(a1, a2 - epsilon, a2 + epsilon, false)
  def rad(deg: Float): Float = joml.Math.toRadians(deg)
  def deg(rad: Float): Float = joml.Math.toDegrees(rad).toFloat
  def sin(rad: Float): Float = joml.Math.sin(rad)
  def cos(rad: Float): Float = joml.Math.cos(rad)
  def tan(rad: Float): Float = joml.Math.tan(rad)
  def asin(rad: Float): Float = joml.Math.asin(rad)
  def acos(rad: Float): Float = joml.Math.acos(rad)
  def atan2(y: Float, x: Float): Float = joml.Math.atan2(y, x)
  def abs(v: Float): Float = joml.Math.abs(v)
  def pow(base: Float, exponent: Float): Float =
    scala.math.pow(base, exponent).toFloat
  def sqrt(n: Float): Float = joml.Math.sqrt(n)

  /** Checks whether 'k' is within two bounds [inclusive].
    *
    * E.g:
    * {{{
    * inBounds(1, 0, 2) == true
    * inBounds(1, 1, 2) == true
    * }}}
    *
    * Automacitally determines order of bounds. E.g:
    * {{{
    * inBounds(1, 0, 2) == true
    * inBounds(1, 2, 0) == true
    * }}}
    */
  def inBounds(k: Float, b1: Float, b2: Float): Boolean =
    if b1 > b2
    then b2 <= k && k <= b1
    else b1 <= k && k <= b2

  /** Checks whether an angle is within two bounds `a` and `b`, when counting
    * counter-clockwise as the positive direction.
    *
    * E.g:
    * {{{
    * angleInBounds(radians(45), radians(0), radians(90)) == true
    * angleInBounds(radians(45), radians(0), radians(90), true) == false
    * angleInBounds(radians(45), radians(90), radians(0), true) == true
    *
    * angleInBounds(radians(-315), radians(0), radians(90)) == true
    * angleInBounds(radians(-315), radians(0), radians(-270), true) == false
    * angleInBounds(radians(45), radians(-270), radians(0), true) == true
    * }}}
    *
    * **Note:** The order of the bounds matter.
    *
    * @param k
    *   \- angle to check
    * @param a
    *   \- first bound [inclusive]
    * @param b
    *   \- second bound [inclusive]
    * @param clockwise
    *   \- whether to count clockwise as the positive direction
    * @return
    */
  def angleInBounds(
      k: Float,
      a: Float,
      b: Float,
      clockwise: Boolean = false
  ): Boolean = {
    val nk = normalAngle(k)
    val na = normalAngle(a)
    val nb = normalAngle(b)

    if (na == nb) {
      nk == na
    } else if (na < nb) {
      if !clockwise
      then inBounds(nk, na, nb)
      else !inBounds(nk, na, nb)
    } else {
      if !clockwise
      then !inBounds(nk, nb, na)
      else inBounds(nk, nb, na)
    }
  }

  /** Checks whether two floats are equal up to a given epsilon value.
    *
    * Take note that floating point inprecisions may occur, for example:
    *   - `nearEquals(0.1f, 0.2f, 0.100000001f)` would return `true` (correct)
    *   - `nearEquals(0.1f, 0.2f, 0.100000000f)` would return `true` (correct)
    *   - `nearEquals(0.1f, 0.2f, 0.099999999f)` would return `true` (incorrect)
    *
    * @param a
    *   the first float
    * @param b
    *   the second float
    * @param epsilon
    *   discrepancy allowed (non-inclusive)
    * @return
    *   `true` if the floats are within epsilon of each other
    */
  def nearEquals(a: Float, b: Float, epsilon: Float = 0.0001f): Boolean = {
    if (a + b) < (a + a)
    then (a + b + epsilon) >= (a + a)
    else (a + b) <= ((a + a) + epsilon)
  }

  // ------------------------
  // implicit conversions
  // ------------------------

  given Conversion[Float, extended_primitives.ImplicitFloat] =
    extended_primitives.ImplicitFloat(_)

  // ------------------------
  // extension methods
  // ------------------------

  implicit class FloatOps(val value: Float) extends AnyVal {
    def toRadians: Float = rad(value)
    def toDegrees: Float = deg(value)
  }
}
