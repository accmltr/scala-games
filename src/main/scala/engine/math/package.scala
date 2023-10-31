package engine

import org.joml

/** Some useful math features for making games.
  */
package object math {
  // ------------------------
  // constants
  // ------------------------
  val pi: Float = scala.math.Pi.toFloat

  // ------------------------
  // operations
  // ------------------------

  def clamp(v: Float, min: Float, max: Float) = joml.Math.clamp(v, min, max)
  def radians(deg: Float): Float = joml.Math.toRadians(deg)
  def degrees(rad: Float): Float = joml.Math.toDegrees(rad).toFloat
  def sin(rad: Float): Float = joml.Math.sin(rad)
  def cos(rad: Float): Float = joml.Math.cos(rad)
  def tan(rad: Float): Float = joml.Math.tan(rad)
  def asin(rad: Float): Float = joml.Math.asin(rad)
  def acos(rad: Float): Float = joml.Math.acos(rad)
  def atan2(y: Float, x: Float): Float = joml.Math.atan2(y, x)
  def abs(v: Float): Float = joml.Math.abs(v)
  def pow(base: Float, exponent: Float): Float =
    math.pow(base, exponent).toFloat
  def sqrt(n: Float): Float = joml.Math.sqrt(n)

  /** Checks whether two floats are equal within a given epsilon.
    *
    * For example:
    *   - `nearEquals(0.1f, 0.2f, 0.10001f)` would return `true`
    *   - `nearEquals(0.1f, 0.2f, 0.10000f)` would return `false`
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
    val diff = a - b
    if diff > 0
    then diff < epsilon
    else diff > -epsilon
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
    def toRadians: Float = radians(value)
    def toDegrees: Float = degrees(value)
  }
}
