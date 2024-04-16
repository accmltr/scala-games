package engine.math

import org.joml.Matrix3f

import scala.annotation.targetName

final case class Matrix3(
                          // format: off
                          c0r0: Float, c0r1: Float, c0r2: Float,
                          c1r0: Float, c1r1: Float, c1r2: Float,
                          c2r0: Float, c2r1: Float, c2r2: Float
                          // format: on
                        ) {

  def transposed: Matrix3 = {
    Matrix3(
      // format: off
      c0r0, c1r0, c2r0, // column 0
      c0r1, c1r1, c2r1, // column 1
      c0r2, c1r2, c2r2 // column 2
      // format: on
    )
  }

  def inverse: Matrix3 = {
    val det = c0r0 * (c1r1 * c2r2 - c1r2 * c2r1) -
      c1r0 * (c0r1 * c2r2 - c0r2 * c2r1) +
      c2r0 * (c0r1 * c1r2 - c0r2 * c1r1)

    if (det == 0) {
      throw new ArithmeticException(
        "Matrix is singular and cannot be inverted."
      )
    }

    val invDet = 1f / det

    Matrix3(
      // format: off
      (c1r1 * c2r2 - c1r2 * c2r1) * invDet,
      (c0r2 * c2r1 - c0r1 * c2r2) * invDet,
      (c0r1 * c1r2 - c0r2 * c1r1) * invDet,
      (c1r2 * c2r0 - c1r0 * c2r2) * invDet,
      (c0r0 * c2r2 - c0r2 * c2r0) * invDet,
      (c0r2 * c1r0 - c0r0 * c1r2) * invDet,
      (c1r0 * c2r1 - c1r1 * c2r0) * invDet,
      (c0r1 * c2r0 - c0r0 * c2r1) * invDet,
      (c0r0 * c1r1 - c0r1 * c1r0) * invDet
      // format: on
    )
  }

  /** Decomposes this matrix into its translation, rotation and scale components.
   *
   * This operation is not injective, meaning that it does not always decompose
   * back into the same values it was made from for its base rotation and scale
   * components. This is since the same matrix can be made with multiple possible
   * component values and vice versa. E.g. you could create a matrix that does a
   * rotation of PI, but that matrix could also be seen as a matrix which scales
   * by x: -1 and y: -1, even though it was created as a rotation matrix.
   */
  def decompose: (Vector2, Float, Vector2) = {
    val scale = Vector2(math.hypot(c0r0, c0r1), math.hypot(c1r0, c1r1))
    val rotation = -math.atan2(c1r0 / scale.y, c0r0 / scale.x).toFloat
    val translation = Vector2(c2r0, c2r1)
    (translation, rotation, scale)
  }

  @targetName("scale")
  def *(scalar: Float): Matrix3 = {
    Matrix3(
      // format: off
      c0r0 * scalar, c0r1 * scalar, c0r2 * scalar, // column 0
      c1r0 * scalar, c1r1 * scalar, c1r2 * scalar, // column 1
      c2r0 * scalar, c2r1 * scalar, c2r2 * scalar // column 2
      // format: on
    )
  }

  @targetName("vec3product")
  def *(v3: Vector3): Vector3 = {
    Vector3(
      (v3.x * c0r0) + (v3.y * c1r0) + (v3.z * c2r0),
      (v3.x * c0r1) + (v3.y * c1r1) + (v3.z * c2r1),
      (v3.x * c0r2) + (v3.y * c1r2) + (v3.z * c2r2)
    )
  }

  @targetName("vec2product")
  def *(v2: Vector2): Vector2 = {
    val result = this * Vector3(v2, 1f)
    Vector2(result.x / result.z, result.y / result.z)
  }

  @targetName("matrixMultiplication")
  def *(other: Matrix3): Matrix3 = {
    Matrix3(
      // format: off
      // column 0
      (other.c0r0 * c0r0) + (other.c0r1 * c1r0) + (other.c0r2 * c2r0),
      (other.c0r0 * c0r1) + (other.c0r1 * c1r1) + (other.c0r2 * c2r1),
      (other.c0r0 * c0r2) + (other.c0r1 * c1r2) + (other.c0r2 * c2r2),
      // column 1
      (other.c1r0 * c0r0) + (other.c1r1 * c1r0) + (other.c1r2 * c2r0),
      (other.c1r0 * c0r1) + (other.c1r1 * c1r1) + (other.c1r2 * c2r1),
      (other.c1r0 * c0r2) + (other.c1r1 * c1r2) + (other.c1r2 * c2r2),
      // column 2
      (other.c2r0 * c0r0) + (other.c2r1 * c1r0) + (other.c2r2 * c2r0),
      (other.c2r0 * c0r1) + (other.c2r1 * c1r1) + (other.c2r2 * c2r1),
      (other.c2r0 * c0r2) + (other.c2r1 * c1r2) + (other.c2r2 * c2r2),
      // format: on
    )
  }

  def toJOMLMatrix3f: Matrix3f = {
    Matrix3f(
      // format: off
      c0r0, c0r1, c0r2, // column 0
      c1r0, c1r1, c1r2, // column 1
      c2r0, c2r1, c2r2 // column 2
      // format: on
    )
  }
}

object Matrix3 {

  val IDENTITY: Matrix3 =
    Matrix3(
      // format: off
      1f, 0f, 0f, // column 0
      0f, 1f, 0f, // column 1
      0f, 0f, 1f // column 2
      // format: on
    )

  def apply(translation: Vector2 = Vector2.zero, rotation: Float = 0, scale: Vector2 = Vector2.one): Matrix3 = {
    Matrix3._translation(translation) * Matrix3._rotation(rotation) * Matrix3._scale(scale)
  }

  /** Create a 3x3 matrix which applies a translation when multiplied with a 2D
   * vector.
   *
   * Example usage:
   * {{{
   * val translationMatrix = Matrix3.translation(1.0f, 2.0f)
   * val vector = Vector2(10.0f, 5.0f)
   * val translatedVector = translationMatrix * vector
   * println(translatedVector) // output: Vector2(11.0f, 7.0f)
   * }}}
   *
   * @param delta
   * A 2D vector representing the x-component and y-component of the
   * translation.
   * @return
   * A 3x3 matrix that represents a 2D translation.
   */
  private def _translation(delta: Vector2): Matrix3 = {
    Matrix3(
      // format: off
      1f, 0f, 0f, // column 0
      0f, 1f, 0f, // column 1
      delta.x, delta.y, 1f // column 2
      // format: on
    )
  }

  /** Create a 3x3 matrix which applies a rotation when multiplied with a 2D
   * vector.
   *
   * Example usage:
   * {{{
   * val translationMatrix = Matrix3.translation(pi/2f) // clockwise rotation
   * val vector = Vector2(10.0f, 5.0f)
   * val translatedVector = translationMatrix * vector
   * println(translatedVector) // output: Vector2(5.0f, -10.0f)
   * }}}
   *
   * @param radians
   * The rotation in radians.
   * @return
   * A 3x3 matrix that represents a 2D translation.
   */
  private def _rotation(radians: Float): Matrix3 = {
    Matrix3(
      // format: off
      cos(radians), sin(radians), 0f, // column 0
      -sin(radians), cos(radians), 0f, // column 1
      0f, 0f, 1f // column 2
      // format: on
    )
  }

  /** Create a 3x3 matrix which applies a scaling when multiplied with a 2D
   * vector.
   *
   * Example usage:
   * {{{
   * val scalingMatrix = Matrix3.scaling(2.0f, 3.0f)
   * val vector = Vector2(10.0f, 5.0f)
   * val scaledVector = scalingMatrix * vector
   * println(scaledVector) // output: Vector2(20.0f, 15.0f)
   * }}}
   *
   * @param scale
   * A 2D vector representing the x-component and y-component of the scaling.
   * @return
   * A 3x3 matrix that represents a 2D scaling.
   */
  private def _scale(scale: Vector2): Matrix3 = {
    Matrix3(
      // format: off
      scale.x, 0f, 0f, // column 0
      0f, scale.y, 0f, // column 1
      0f, 0f, 1f // column 2
      // format: on
    )
  }
}
