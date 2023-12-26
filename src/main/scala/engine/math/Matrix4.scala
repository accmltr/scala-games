package engine.math

final case class Matrix4(
    m00: Float = 1.0f,
    m01: Float = 0.0f,
    m02: Float = 0.0f,
    m03: Float = 0.0f,
    m10: Float = 0.0f,
    m11: Float = 1.0f,
    m12: Float = 0.0f,
    m13: Float = 0.0f,
    m20: Float = 0.0f,
    m21: Float = 0.0f,
    m22: Float = 1.0f,
    m23: Float = 0.0f,
    m30: Float = 0.0f,
    m31: Float = 0.0f,
    m32: Float = 0.0f,
    m33: Float = 1.0f
) {
  def *(other: Matrix4): Matrix4 = {
    Matrix4(
      m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30,
      m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31,
      m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32,
      m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33,
      m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30,
      m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31,
      m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32,
      m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33,
      m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30,
      m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31
    )
  }
}
