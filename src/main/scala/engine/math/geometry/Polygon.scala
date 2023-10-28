package engine.math.geometry

import engine.math._
import scala.annotation.tailrec

case class Polygon(points: List[Vector2])
    extends Shape2D,
      NearEqualsable[Polygon] {

  val isClockwise: Boolean = _isPolygonClockwise

  private val polygonIntersection =
    new org.joml.PolygonsIntersection(
      points.flatMap(p => List(p.x, p.y)).toArray,
      Array(0),
      points.length
    ) // Assuming the polygon is not made of multiple disjoint polygons

  def translate(translation: Vector2): Polygon = {
    Polygon(points.map(_ + translation))
  }
  def rotate(degrees: Float): Polygon = ???
  def rotateAround(degrees: Float, point: Vector2): Polygon = ???

  // use joml for contains
  override def contains(point: Vector2): Boolean = {
    polygonIntersection.testPoint(point.x, point.y)
  }

  def scale(amount: Float): Polygon = {
    Polygon(points.map(_ * amount))
  }
  override def grow(amount: Float): Polygon = {
    // keep each edge parallel to the original edge and move it outwards by amount

    @tailrec
    def recGrow(
        remaining: List[Vector2],
        previous: Vector2 = null,
        first: Vector2 = null,
        second: Vector2 = null,
        acc: List[Vector2] = Nil
    ): List[Vector2] =
      remaining match {
        case Nil =>
          if first == null || second == null
          then Nil // if a list with less than 3 points is passed
          else
            var p: Vector2 = makeNewPoint(
              previous,
              first,
              second
            )
            p :: acc // if a non-empty list has been computed to its end
        case head :: next =>
          if first == null
          then recGrow(next, head, head, null, acc)
          else if second == null
          then
            if next.isEmpty
            then recGrow(next, head, first, null, acc)
            else
              var p: Vector2 = makeNewPoint(
                first,
                head,
                next.head
              )
              recGrow(next, head, first, head, p :: acc)
          else
            var p: Vector2 = makeNewPoint(
              previous,
              head,
              if next.nonEmpty then next.head else first
            )
            recGrow(next, head, first, second, p :: acc) // recursion
      }

    def makeNewPoint(
        p1: Vector2,
        p2: Vector2,
        p3: Vector2
    ): Vector2 =
      val edge1: Vector2 = if isClockwise then p3 - p2 else p2 - p1
      val edge2: Vector2 = if isClockwise then p2 - p1 else p3 - p2
      val halfEdgeAngle: Float = edge1.angleBetween(edge2) / 2f
      val l: Float = amount / sin(halfEdgeAngle)
      val a =
        if isClockwise
        then edge1.angle + pi - halfEdgeAngle
        else edge2.angle + pi + halfEdgeAngle
      var offset = Vector2.fromAngle(a, l)
      val newPoint: Vector2 =
        p2 + offset
      newPoint

    Polygon(recGrow(points))
  }
  def toPolygon: Polygon = this

  def nearEquals(other: Polygon, epsilon: Float = 0.0001f): Boolean = {

    @tailrec
    def checkPoints(points1: List[Vector2], points2: List[Vector2]): Boolean = {
      points1 match {
        case head1 :: next1 =>
          points2 match {
            case head2 :: next2 =>
              if head1 nearEquals head2
              then checkPoints(next1, next2)
              else false
            case Nil => false
          }
        case Nil => points2.isEmpty
      }
    }

    if points.length != other.points.length
    then false
    else checkPoints(points, other.points)
  }
  override def equals(x: Any): Boolean = x match {
    case Polygon(points) => this.points == points
    case that: Shape2D =>
      points == that.toPolygon.points
    case _ => false
  }

  private def _isPolygonClockwise: Boolean = {
    val sum = points.zip(points.tail :+ points.head).foldLeft(0f) {
      case (acc, (p1, p2)) =>
        acc + (p2.x - p1.x) * (p2.y + p1.y)
    }
    sum > 0
  }
}
