package engine.math

import org.joml.Matrix3f

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
      c0r0, c1r0, c2r0,  // column 0
      c0r1, c1r1, c2r1,  // column 1
      c0r2, c1r2, c2r2   // column 2
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

  def translationValue: Vector2 = Vector2(c2r0, c2r1)
  def rotationValue: Float = math.atan2(c1r0, c0r0).toFloat
  def scalingValue: Vector2 = Vector2(c0r0, c1r1)
  def translationMatrix = Matrix3.translation(translationValue)
  def rotationMatrix = Matrix3.rotation(rotationValue)
  def scalingMatrix = Matrix3.scaling(scalingValue)

  def translated(x: Float, y: Float): Matrix3 = {
    this * Matrix3.translation(x, y)
  }

  def rotated(radians: Float): Matrix3 = {
    this * Matrix3.rotation(radians)
  }

  def scaled(x: Float, y: Float): Matrix3 = {
    this * Matrix3.scaling(x, y)
  }

  def scaled(v: Vector2): Matrix3 = {
    this * Matrix3.scaling(v)
  }

  def *(scalar: Float): Matrix3 = {
    Matrix3(
      // format: off
      c0r0 * scalar, c0r1 * scalar, c0r2 * scalar,  // column 0
      c1r0 * scalar, c1r1 * scalar, c1r2 * scalar,  // column 1
      c2r0 * scalar, c2r1 * scalar, c2r2 * scalar   // column 2
      // format: on
    )
  }

  def *(v3: Vector3): Vector3 = {
    Vector3(
      (v3.x * c0r0) + (v3.y * c1r0) + (v3.z * c2r0),
      (v3.x * c0r1) + (v3.y * c1r1) + (v3.z * c2r1),
      (v3.x * c0r2) + (v3.y * c1r2) + (v3.z * c2r2)
    )
  }

  def *(v2: Vector2): Vector2 = {
    val result = (this * Vector3(v2, 1f))
    Vector2(result.x / result.z, result.y / result.z)
  }

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

  def toJomlMatrix3f: Matrix3f = {
    Matrix3f(
      // format: off
      c0r0, c0r1, c0r2,  // column 0
      c1r0, c1r1, c1r2,  // column 1
      c2r0, c2r1, c2r2   // column 2
      // format: on
    )
  }
}

object Matrix3 {

  val IDENTITY: Matrix3 =
    Matrix3(
          // format: off
          1f, 0f, 0f,  // column 0
          0f, 1f, 0f,  // column 1
          0f, 0f, 1f   // column 2
          // format: on
    )

  def apply(
      translation: Vector2 = Vector2.zero,
      rotation: Float = 0,
      scale: Vector2 = Vector2.one
  ): Matrix3 = {
    val rot = Matrix3.rotation(rotation)
    val sca = Matrix3.scaling(scale)
    val tra = Matrix3.translation(translation)
    tra * rot * sca
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
    * @param x
    *   The x-component of the translation.
    * @param y
    *   The y-component of the translation.
    * @return
    *   A 3x3 matrix that represents a 2D translation.
    */
  def translation(x: Float, y: Float): Matrix3 = {
    Matrix3(
      // format: off
      1f, 0f, 0f,  // column 0
      0f, 1f, 0f,  // column 1
      x, y, 1f     // column 2
      // format: on
    )
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
    *   A 2D vector representing the x-component and y-component of the
    *   translation.
    * @return
    *   A 3x3 matrix that represents a 2D translation.
    */
  def translation(delta: Vector2): Matrix3 = {
    translation(delta.x, delta.y)
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
    *   The rotation in radians.
    * @return
    *   A 3x3 matrix that represents a 2D translation.
    */
  def rotation(radians: Float): Matrix3 = {
    Matrix3(
      // format: off
      cos(radians), -sin(radians), 0f, // column 0
      sin(radians), cos(radians), 0f,  // column 1
      0f, 0f, 1f                       // column 2
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
    * @param x
    *   The x-component of the scaling.
    * @param y
    *   The y-component of the scaling.
    * @return
    *   A 3x3 matrix that represents a 2D scaling.
    */
  def scaling(x: Float, y: Float): Matrix3 = {
    Matrix3(
      // format: off
      x, 0f, 0f,  // column 0
      0f, y, 0f,  // column 1
      0f, 0f, 1f  // column 2
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
    *   A 2D vector representing the x-component and y-component of the scaling.
    * @return
    *   A 3x3 matrix that represents a 2D scaling.
    */
  def scaling(scale: Vector2): Matrix3 = {
    scaling(scale.x, scale.y)
  }
}
