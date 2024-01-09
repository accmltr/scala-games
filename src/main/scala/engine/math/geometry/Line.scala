package engine.math.geometry

import engine.math.Vector2
import engine.math.pi

final case class Line(a: Vector2, b: Vector2) {

  def length: Float = a.distance(b)
  def lengthSquared: Float = a.distanceSquared(b)

  def angle: Float = {
    val dx = b.x - a.x
    val dy = b.y - a.y
    math.atan2(dy, dx).toFloat
  }

  def angleBetween(other: Line): Float = {
    val angle1 = angle
    val angle2 = other.angle
    val diff = math.abs(angle1 - angle2)
    if diff > pi
    then 2 * pi - diff
    else diff
  }

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
    if (d == 0) None // parallel
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

  override def equals(x: Any): Boolean =
    x match {
      case l: Line => (l.a == a && l.b == b) || (l.a == b && l.b == a)
      case _       => false
    }
}
