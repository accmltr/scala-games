package engine.math

package object geometry {
  def linesIntersect(
      a: (Vector2, Vector2),
      b: (Vector2, Vector2)
  ): Boolean = {
    val (a1, a2) = a
    val (b1, b2) = b

    val (a1x, a1y) = (a1.x, a1.y)
    val (a2x, a2y) = (a2.x, a2.y)
    val (b1x, b1y) = (b1.x, b1.y)
    val (b2x, b2y) = (b2.x, b2.y)

    val d = (a1x - a2x) * (b1y - b2y) - (a1y - a2y) * (b1x - b2x)

    if (d == 0) return false

    val u =
      ((a2x - b2x) * (b1y - b2y) - (a2y - b2y) * (b1x - b2x)) / d
    val v =
      ((a2x - a1x) * (a1y - b2y) - (a2y - a1y) * (a1x - b2x)) / d

    u >= 0 && u <= 1 && v >= 0 && v <= 1
  }
}
