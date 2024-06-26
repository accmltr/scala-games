package engine.math

import org.joml

import scala.language.implicitConversions

case class Vector2(x: Float, y: Float) extends NearEqualsable[Vector2] {
  def +(o: Vector2): Vector2 = Vector2(x + o.x, y + o.y)

  def -(o: Vector2): Vector2 = Vector2(x - o.x, y - o.y)

  def *(f: Float): Vector2 = Vector2(x * f, y * f)

  def /(f: Float): Vector2 = this * (1 / f)

  def hadamard(other: Vector2): Vector2 =
    Vector2(x * other.x, y * other.y)

  def cross(other: Vector2): Float =
    x * other.y - y * other.x

  def dot(other: Vector2): Float =
    x * other.x + y * other.y

  def length: Float = joml.Vector2f.length(x, y)

  /** More efficient than the `length` method, because it doesn't do the square
   * root. Try to use this instead of `length` when possible.
   *
   * @return
   * length of this `Vector2` squared
   */
  def lengthSquared: Float = joml.Vector2f.lengthSquared(x, y)

  def normalize: Vector2 = {
    val len = length
    if len == 0 then Vector2.zero
    else Vector2(x / len, y / len)
  }

  /** Calculates the length of the space between this Vector2 and another.
   *
   * Always returns a non-negative value
   */
  def distanceTo(other: Vector2): Float =
    joml.Vector2f.distance(x, y, other.x, other.y)

  /** More efficient than the `distanceTo` method, because it skips doing the
   * square-root when calculating. Try to use this instead of `distance` when
   * possible.
   *
   * ### Example
   * {{{
   * // This is inside an imaginary "Character" class, which extends Entity.
   * def inRange(target: Character, range: Float): Boolean =
   *   val d = globalPosition.distanceToSquared(target.globalPosition)
   *   d <= range * range
   * }}}
   *
   * @return Distance squared between this and another `Vector2`.
   */
  def distanceToSquared(other: Vector2): Float = {
    joml.Vector2f.distanceSquared(x, y, other.x, other.y)
  }

  /** Return vector rotated by given angle.
   *
   * @param angle
   * should be in radians
   */
  def rotated(angle: Float): Vector2 = {
    Vector2(x * cos(angle) - y * sin(angle), x * sin(angle) + y * cos(angle))
  }

  /** Returns angle of vector (in radians).
   */
  def angle: Float = normalAngle(rawAngle)

  /** Returns the angle of the vector (in radians), without normalizing it.
   *
   * This can be used to save a bit on performance in some scenarios.
   */
  def rawAngle: Float = math.atan2(y, x).toFloat

  /** Returns the angle between this vector and another vector, if you drew a
   * line from the origin to each vector.
   */
  def angleBetween(other: Vector2): Float =
    val v1 = Vector3(x, y, 0)
    val v2 = Vector3(other.x, other.y, 0)
    val dot = v1.dot(v2)
    val det = v1.cross(v2).length
    math.atan2(det, dot).toFloat

  /** Returns the angle of the vector that starts on the position of this vector, and ends on the position of the `other` vector.
   */
  def angleTo(other: Vector2): Float =
    (other - this).angle

  def nearEquals(other: Vector2, epsilon: Float = 0.0001f): Boolean = {
    x.nearEquals(other.x, epsilon) && y.nearEquals(other.y, epsilon)
  }

  override def equals(other: Any): Boolean = {
    other match
      case v: Vector2 => x == v.x && y == v.y
      case _ => false
  }

  override def toString: String = "(%f, %f)".format(x, y)

  /** Returns a new vector with the given number of decimals.
   */
  def formatted(decimals: Int): String = {
    val format = "%%.%df".format(decimals)
    "(%s, %s)".format(format.format(x), format.format(y))
  }
}

object Vector2 {
  def apply(x: Double, y: Double) = new Vector2(x.toFloat, y.toFloat)

  def apply(x: Float, y: Float) = new Vector2(x, y)

  def apply(x: Int, y: Int) = new Vector2(x, y)

  inline def zero: Vector2 = apply(0, 0)

  inline def one: Vector2 = apply(1, 1)

  inline def xAxis: Vector2 = apply(1, 0)

  inline def yAxis: Vector2 = apply(0, 1)

  /** Creates a vector given an angle and a length.
   *
   * @param angle
   * should be in radians
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
