package engine.math

import org.joml

/** Library for doing math with Floats instead of Doubles (like in scala.math),
  * and other useful math for making games.
  */
object Operations {
  def clamp(v: Float, min: Float, max: Float) = joml.Math.clamp(v, min, max)
  def toRadians(deg: Float): Float = joml.Math.toRadians(deg)
  def toDegrees(rad: Float): Float = joml.Math.toDegrees(rad).toFloat
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
  def nearEquals(a: Float, b: Float, epsilon: Float = 0.0001f): Boolean =
    joml.Math.abs(a - b) <= epsilon
}

object Extensions {
  implicit class FloatOps(val value: Float) extends AnyVal {
    def toRadians: Float = Operations.toRadians(value)
    def toDegrees: Float = Operations.toDegrees(value)
  }
}
