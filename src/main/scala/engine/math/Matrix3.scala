package engine.math

final case class Matrix3(
    m00: Float = 1.0f,
    m01: Float = 0.0f,
    m02: Float = 0.0f,
    m10: Float = 0.0f,
    m11: Float = 1.0f,
    m12: Float = 0.0f,
    m20: Float = 0.0f,
    m21: Float = 0.0f,
    m22: Float = 1.0f
) {
  def *(other: Matrix3): Matrix3 = {
    Matrix3(
      m00 * other.m00 + m01 * other.m10 + m02 * other.m20,
      m00 * other.m01 + m01 * other.m11 + m02 * other.m21,
      m00 * other.m02 + m01 * other.m12 + m02 * other.m22,
      m10 * other.m00 + m11 * other.m10 + m12 * other.m20,
      m10 * other.m01 + m11 * other.m11 + m12 * other.m21,
      m10 * other.m02 + m11 * other.m12 + m12 * other.m22,
      m20 * other.m00 + m21 * other.m10 + m22 * other.m20,
      m20 * other.m01 + m21 * other.m11 + m22 * other.m21,
      m20 * other.m02 + m21 * other.m12 + m22 * other.m22
    )
  }
}

object Matrix3 {

  val IDENTITY = Matrix3()

  def apply(position: Vector2, rotation: Float, scale: Float): Matrix3 = {
    val cos = Math.cos(rotation).toFloat
    val sin = Math.sin(rotation).toFloat
    Matrix3(
      cos * scale,
      -sin * scale,
      position.x,
      sin * scale,
      cos * scale,
      position.y,
      0.0f,
      0.0f,
      1.0f
    )
  }
}
