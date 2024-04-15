package engine.math.geometry

import engine.math.Vector2
import engine.math.pi

/** The order of 'a' and 'b' matters for some operations, e.g. 'Line.angle'.
  *
  * @param a
  * @param b
  */
final case class Line(a: Vector2, b: Vector2) {

  def length: Float = a.distanceTo(b)
  def lengthSquared: Float = a.distanceToSquared(b)

  def angle: Float = {
    val dx = b.x - a.x
    val dy = b.y - a.y
    math.atan2(dy, dx).toFloat
  }

  def normal = Vector2(a.y - b.y, b.x - a.x).normalize

  def angleBetween(other: Line): Float = {
    val angle1 = angle
    val angle2 = other.angle
    val diff = math.abs(angle1 - angle2)
    if diff > pi
    then 2 * pi - diff
    else diff
  }

  def intersects(line: Line, includeEndpoints: Boolean = true): Boolean =
    if !includeEndpoints && (line.a == a || line.a == b || line.b == a || line.b == b)
    then false
    else
      intersection(line) match {
        case Some(pt) => true
        case None     => false
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
    else if ((point.x - a.x) * (b.y - a.y) == (point.y - a.y) * (b.x - a.x)) {
      val x = point.x
      val y = point.y
      val x1 = a.x
      val y1 = a.y
      val x2 = b.x
      val y2 = b.y
      if (x1 == x2) {
        y1 <= y && y <= y2 || y2 <= y && y <= y1
      } else {
        x1 <= x && x <= x2 || x2 <= x && x <= x1
      }
    } else false

  override def equals(x: Any): Boolean =
    x match {
      case l: Line => (l.a == a && l.b == b) || (l.a == b && l.b == a)
      case _       => false
    }
}
