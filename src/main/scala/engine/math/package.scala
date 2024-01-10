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
    *  positiveAngle(radians(-315)) == radians(45)
    *  positiveAngle(raFdians(45)) == radians(45)
    *  positiveAngle(radians(-90)) == radians(270)
    * }}}
    *
    * @param rad
    *   Angle in radians
    * @return
    */
  def positiveAngle(rad: Float): Float = {
    val modded = rad % (2 * pi)
    val pos =
      if modded < 0
      then modded + (2 * pi)
      else modded
    if pos == 2 * pi then 0 else pos
  }
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

  /** Checks whether an angle is within a lower and upper bound range, when
    * counting counter-clockwise as the positive direction.
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
    * @param angle
    *   The angle in radians
    * @param lower
    *   The lower bound in radians
    * @param upper
    *   The upper bound in radians
    * @return
    */
  def angleInBounds(
      k: Float,
      a: Float,
      b: Float,
      clockwise: Boolean = false
  ): Boolean = {
    val kp = positiveAngle(k)
    val ap = positiveAngle(a)
    val bp = positiveAngle(b)

    if (ap < bp) {
      if !clockwise
      then inBounds(kp, ap, bp)
      else !inBounds(kp, ap, bp)
    } else {
      if !clockwise
      then !inBounds(kp, bp, ap)
      else inBounds(kp, bp, ap)
    }

  }

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
