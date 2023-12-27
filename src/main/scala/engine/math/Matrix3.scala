package engine.math

import org.joml.Matrix3f

final case class Matrix3(
    // format: off
    c0r0: Float, c0r1: Float, c0r2: Float,
    c1r0: Float, c1r1: Float, c1r2: Float,
    c2r0: Float, c2r1: Float, c2r2: Float
    // format: on
) {
  def toJomlMatrix3f: Matrix3f = {
    Matrix3f(
      // format: off
      c0r0, c1r0, c2r0, // row 1
      c0r1, c1r1, c2r1, // row 2
      c0r2, c1r2, c2r2 // row 3
      // format: on
    )
  }
}
