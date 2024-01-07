package engine.math.geometry

import engine.math.Vector2

final case class Line(a: Vector2, b: Vector2) {
  def intersects(line: Line, includeEndpoints: Boolean = true): Boolean =
    intersection(line) match {
      case Some(pt) =>
        if includeEndpoints
        then true
        else pt != a && pt != b && pt != line.a && pt != line.b
      case None =>
        false
    }

  def intersection(line: Line): Option[Vector2] = {
    val d = (a.x - b.x) * (line.a.y - line.b.y) -
      (a.y - b.y) * (line.a.x - line.b.x)
    if (d == 0) None
    else {
      val u = ((line.b.x - line.a.x) * (a.y - line.a.y) -
        (line.b.y - line.a.y) * (a.x - line.a.x)) / d
      val v = ((b.x - a.x) * (a.y - line.a.y) -
        (b.y - a.y) * (a.x - line.a.x)) / d
      if (u < 0 || u > 1 || v < 0 || v > 1) None
      else
        Some(
          Vector2(
            a.x + u * (b.x - a.x),
            a.y + u * (b.y - a.y)
          )
        )
    }
  }

  def overlaps(line: Line, includeEndpoints: Boolean = true): Boolean =
    contains(line.a, includeEndpoints) ||
      contains(line.b, includeEndpoints) ||
      intersects(line, includeEndpoints)

  def contains(point: Vector2, includeEndpoints: Boolean = true): Boolean =
    if !includeEndpoints && (point == a || point == b)
    then false
    else (point.x - a.x) * (b.y - a.y) == (point.y - a.y) * (b.x - a.x)
}
