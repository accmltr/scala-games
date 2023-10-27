package engine.math

import engine.math.Operations._
import org.joml
import engine.test_utils.NearEqualsable

case class Vector2(val x: Float, val y: Float) extends NearEqualsable[Vector2] {
  def +(o: Vector2) = Vector2(x + o.x, y + o.y)
  def -(o: Vector2) = Vector2(x - o.x, y - o.y)
  def *(f: Float) = Vector2(x * f, y * f)
  def /(f: Float) = this * (1 / f)

  def length: Float = joml.Vector2f.length(x, y)

  /** More efficient than the `length` method, because it doesn't do the square
    * root. Try to use this instead of `length` when possible.
    *
    * @return
    *   length of this `Vector2` squared
    */
  def lengthSquared: Float = joml.Vector2f.lengthSquared(x, y)
  def normalize = this / length
  def distance(other: Vector2) = joml.Vector2f.distance(x, y, other.x, other.y)

  /** More efficient than the `distance` method, because it doesn't do the
    * square root. Try to use this instead of `distance` when possible.
    *
    * @param other
    * @return
    *   distance squared between this and another `Vector2`
    */
  def distanceSquared(other: Vector2) =
    joml.Vector2f.distanceSquared(x, y, other.x, other.y)

  /** Return vector rotated by given angle.
    *
    * @param angle
    *   should be in radians
    */
  def rotated(angle: Float) =
    Vector2(x * cos(angle) - y * sin(angle), x * sin(angle) + y * cos(angle))

  /** Returns angle of vector (in radians).
    */
  def angle: Float = math.atan2(y, x).toFloat
  def angleBetween(other: Vector2): Float = {
    val v1 = Vector3(x, y, 0)
    val v2 = Vector3(other.x, other.y, 0)
    val dot = v1 dot v2
    val det = (v1 cross v2).length
    math.atan2(det, dot).toFloat
  }

  def nearEquals(other: Vector2, epsilon: Float = 0.0001f): Boolean =
    Operations.nearEqual(x, other.x, epsilon) &&
      Operations.nearEqual(y, other.y, epsilon)

  override def equals(other: Any) = other match
    case v: Vector2 => x == v.x && y == v.y
    case _          => false

  override def toString = "(%f, %f)".format(x, y)

  /** Returns a new vector with the given number of decimals.
    */
  def formatted(decimals: Int): String =
    val format = "%%.%df".format(decimals)
    "(%s, %s)".format(format.format(x), format.format(y))
}

object Vector2 {
  def apply(x: Double, y: Double) = new Vector2(x.toFloat, y.toFloat)
  def apply(x: Float, y: Float) = new Vector2(x, y)
  def apply(x: Int, y: Int) = new Vector2(x, y)

  def zero = apply(0, 0)
  def one = apply(1, 1)
  def xAxis = apply(1, 0)
  def yAxis = apply(0, 1)

  /** Creates a vector given an angle and a length.
    *
    * @param angle
    *   should be in radians
    */
  def fromAngle(angle: Float, length: Float = 1) =
    new Vector2(cos(angle) * length, sin(angle) * length)
}

object Vector2Implicits {
  implicit def iTupleToVector2(tuple: (Int, Int)): Vector2 = {
    Vector2(tuple._1, tuple._2)
  }

  implicit def fTupleToVector2(tuple: (Float, Float)): Vector2 = {
    Vector2(tuple._1, tuple._2)
  }

  implicit def dTupleToVector2(tuple: (Double, Double)): Vector2 = {
    Vector2(tuple._1.toFloat, tuple._2.toFloat)
  }

  implicit def toJomlVector2(v: Vector2): joml.Vector2f =
    new joml.Vector2f(v.x, v.y)

  implicit def fromJomlVector2(v: joml.Vector2f): Vector2 =
    Vector2(v.x, v.y)

}
