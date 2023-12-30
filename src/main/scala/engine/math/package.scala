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
    scala.math.pow(base, exponent).toFloat
  def sqrt(n: Float): Float = joml.Math.sqrt(n)

  /** Checks whether 'f' is within a lower and upper bound range [inclusive].
    *
    * @param f
    * @param lowerBound
    *   Lower bound [inclusive]
    * @param upperBound
    *   Upper bound [inclusive]
    * @return
    */
  def inBounds(f: Float, lowerBound: Float, upperBound: Float): Boolean =
    if lowerBound > upperBound
    then throw new Exception("Lower bound may not be greater than upper bound")
    else lowerBound <= f && f <= upperBound

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
    def toRadians: Float = radians(value)
    def toDegrees: Float = degrees(value)
  }
}
