package engine.math

import org.joml.Matrix4f

final case class Matrix4(
    // format: off
    c0r0: Float, c0r1: Float, c0r2: Float,c0r3: Float,
    c1r0: Float, c1r1: Float, c1r2: Float, c1r3: Float,
    c2r0: Float, c2r1: Float, c2r2: Float, c2r3: Float,
    c3r0: Float, c3r1: Float, c3r2: Float, c3r3: Float
    // format: on
) {
  def toJomlMatrix4f: Matrix4f = {
    Matrix4f(
      // format: off
      c0r0, c0r1, c0r2,c0r3, // column 1
      c1r0, c1r1, c1r2,c1r3, // column 2
      c2r0, c2r1, c2r2,c2r3, // column 3
      c3r0, c3r1, c3r2,c3r3, // column 4
      // format: on
    )
  }
}
