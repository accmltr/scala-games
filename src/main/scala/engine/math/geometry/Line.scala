package engine.math.geometry

import engine.math.Vector2

final case class Line(pointA: Vector2, pointB: Vector2) {
  def intersects(line: Line): Boolean =
    (pointA.x - pointB.x) * (line.pointA.y - line.pointB.y) -
      (pointA.y - pointB.y) * (line.pointA.x - line.pointB.x) != 0
}
