package engine.math.geometry

import engine.math.Vector2

final case class Line(pointA: Vector2, pointB: Vector2) {
  def intersects(line: Line, includeEndpoints: Boolean = true): Boolean =
    if (includeEndpoints) {
      val d1 = (pointB.x - pointA.x) * (line.pointA.y - pointA.y) -
        (pointB.y - pointA.y) * (line.pointA.x - pointA.x)
      val d2 = (pointB.x - pointA.x) * (line.pointB.y - pointA.y) -
        (pointB.y - pointA.y) * (line.pointB.x - pointA.x)
      val d3 = (line.pointB.x - line.pointA.x) * (pointA.y - line.pointA.y) -
        (line.pointB.y - line.pointA.y) * (pointA.x - line.pointA.x)
      val d4 = (line.pointB.x - line.pointA.x) * (pointB.y - line.pointA.y) -
        (line.pointB.y - line.pointA.y) * (pointB.x - line.pointA.x)

      d1 * d2 <= 0 && d3 * d4 <= 0
    } else {
      val d1 = (pointB.x - pointA.x) * (line.pointA.y - pointA.y) -
        (pointB.y - pointA.y) * (line.pointA.x - pointA.x)
      val d2 = (pointB.x - pointA.x) * (line.pointB.y - pointA.y) -
        (pointB.y - pointA.y) * (line.pointB.x - pointA.x)
      val d3 = (line.pointB.x - line.pointA.x) * (pointA.y - line.pointA.y) -
        (line.pointB.y - line.pointA.y) * (pointA.x - line.pointA.x)
      val d4 = (line.pointB.x - line.pointA.x) * (pointB.y - line.pointA.y) -
        (line.pointB.y - line.pointA.y) * (pointB.x - line.pointA.x)

      d1 * d2 < 0 && d3 * d4 < 0
    }
    // (pointA.x - pointB.x) * (line.pointA.y - line.pointB.y) -
    //   (pointA.y - pointB.y) * (line.pointA.x - line.pointB.x) != 0
}
