package engine.math

import engine.math.Operations._

case class Vector3(val x: Float, val y: Float, val z: Float) {
  def +(o: Vector3) = Vector3(x + o.x, y + o.y, z + o.z)
  def -(o: Vector3) = Vector3(x - o.x, y - o.y, z - o.z)
  def *(f: Float) = Vector3(x * f, y * f, z * f)
  def /(f: Float) = this * (1 / f)

  def length: Float = sqrt(x * x + y * y + z * z)
  def normalize = this / length

  /** Returns the dot product of this vector with another vector.
    */
  def dot(o: Vector3): Float = x * o.x + y * o.y + z * o.z

  /** Returns the cross product of this vector with another vector.
    */
  def cross(o: Vector3): Vector3 =
    Vector3(y * o.z - z * o.y, z * o.x - x * o.z, x * o.y - y * o.x)

  /** Returns angle between this vector and another vector (in radians).
    */
  def angleWith(o: Vector3): Float = {
    val cosAngle = (this dot o) / (this.length * o.length)
    math.acos(cosAngle).toFloat
  }

  override def equals(other: Any) = other match {
    case v: Vector3 => x == v.x && y == v.y && z == v.z
    case _          => false
  }

  override def toString = "(%f, %f, %f)".format(x, y, z)

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

  def zero = apply(0, 0, 0)
  def one = apply(1, 1, 1)
  def left = apply(-1, 0, 0)
  def right = apply(1, 0, 0)
  def up = apply(0, 1, 0)
  def down = apply(0, -1, 0)
  def forward = apply(0, 0, 1)
  def backward = apply(0, 0, -1)

  /** Creates a vector given spherical coordinates (radius, azimuth, and
    * inclination). Azimuth and inclination should be in radians.
    */
  def fromSpherical(
      radius: Float,
      azimuth: Float,
      inclination: Float
  ): Vector3 = {
    val cosInc = cos(inclination)
    Vector3(
      radius * cos(azimuth) * cosInc,
      radius * sin(azimuth) * cosInc,
      radius * sin(inclination)
    )
  }
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
}
