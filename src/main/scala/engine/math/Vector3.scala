package engine.math

import org.joml

import scala.language.implicitConversions

case class Vector3(x: Float, y: Float, z: Float)
    extends NearEqualsable[Vector3] {

  /** Creates a 2D vector from the x- and y-components of this 3D vector.
    */
  def xy: Vector2 = Vector2(x, y)

  def +(o: Vector3): Vector3 = Vector3(x + o.x, y + o.y, z + o.z)
  def -(o: Vector3): Vector3 = Vector3(x - o.x, y - o.y, z - o.z)
  def *(f: Float): Vector3 = Vector3(x * f, y * f, z * f)
  def /(f: Float): Vector3 = this * (1 / f)

  def length: Float = joml.Vector3f.length(x, y, z)

  /** More efficient than the `length` method, because it doesn't do the square
    * root. Try to use this instead of `length` when possible.
    *
    * @return
    *   length of this `Vector3` squared
    */
  def lengthSquared: Float = joml.Vector3f.lengthSquared(x, y, z)
  def distance(other: Vector3): Float =
    joml.Vector3f.distance(x, y, z, other.x, other.y, other.z)

  /** More efficient than the `distance` method, because it doesn't do the
    * square root. Try to use this instead of `distance` when possible.
    * @return
    *   distance squared between this and another `Vector3`
    */
  def distanceSquared(other: Vector3): Float =
    joml.Vector3f.distanceSquared(x, y, z, other.x, other.y, other.z)
  def normalize: Vector3 = this / length

  /** Returns the dot product of this vector with another vector.
    */
  def dot(other: Vector3): Float = x * other.x + y * other.y + z * other.z

  /** Returns the cross product of this vector with another vector.
    */
  def cross(other: Vector3): Vector3 =
    Vector3(
      y * other.z - z * other.y,
      z * other.x - x * other.z,
      x * other.y - y * other.x
    )

  /** Returns angle between this vector and another vector (in radians).
    */
  def angleBetween(other: Vector3): Float = {
    val cosAngle = this.dot(other) / (this.length * other.length)
    math.acos(cosAngle).toFloat
  }

  def nearEquals(other: Vector3, epsilon: Float = 0.0001f): Boolean = {
    x.nearEquals(other.x, epsilon) &&
      y.nearEquals(other.y, epsilon) &&
      z.nearEquals(other.z, epsilon)
  }

  override def equals(other: Any): Boolean = other match {
    case v: Vector3 => x == v.x && y == v.y && z == v.z
    case _          => false
  }

  override def toString: String = "(%f, %f, %f)".format(x, y, z)

  /** Returns a new vector with the given number of decimals.
    */
  def formatted(decimals: Int): String = {
    val format = "%%.%df".format(decimals)
    "(%s, %s, %s)".format(format.format(x), format.format(y), format.format(z))
  }
}

object Vector3 {
  def apply(x: Double, y: Double, z: Double) =
    new Vector3(x.toFloat, y.toFloat, z.toFloat)
  def apply(x: Float, y: Float, z: Float) = new Vector3(x, y, z)
  def apply(x: Int, y: Int, z: Int) = new Vector3(x, y, z)
  def apply(vec2: Vector2, z: Float | Int | Double): Vector3 =
    val zf: Float = z match {
      case i: Int    => i.toFloat
      case i: Double => i.toFloat
      case i: Float  => i
    }
    new Vector3(vec2.x, vec2.y, zf)

  def zero: Vector3 = apply(0, 0, 0)
  def one: Vector3 = apply(1, 1, 1)
  def xAxis: Vector3 = apply(1, 0, 0)
  def yAxis: Vector3 = apply(0, 1, 0)
  def zAxis: Vector3 = apply(0, 0, 1)
}

object Vector3Implicits {
  implicit def iTupleToVector3(tuple: (Int, Int, Int)): Vector3 = {
    Vector3(tuple._1, tuple._2, tuple._3)
  }

  implicit def fTupleToVector3(tuple: (Float, Float, Float)): Vector3 = {
    Vector3(tuple._1, tuple._2, tuple._3)
  }

  implicit def dTupleToVector3(tuple: (Double, Double, Double)): Vector3 = {
    Vector3(tuple._1.toFloat, tuple._2.toFloat, tuple._3.toFloat)
  }

  implicit def toJomlVector3(v: Vector3): joml.Vector3f =
    new joml.Vector3f(v.x, v.y, v.z)

  implicit def fromJomlVector3(v: joml.Vector3f): Vector3 =
    Vector3(v.x, v.y, v.z)
}
