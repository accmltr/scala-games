package engine.math.shapes

import engine.math.*

import scala.annotation.{tailrec, targetName}
import scala.util.boundary
import boundary.break
import engine.math.geometry.Line

case class Polygon(points: Vector[Vector2])
    extends Shape2D
    with NearEqualsable[Polygon] {
  if (points.size < 3)
    throw RuntimeException("Polygon must have at least 3 points defining it")
  if (_hasDuplicates)
    throw RuntimeException(
      "Polygon may not have any 2 points that are equal. [This is to simplify operations internally and gain performance]"
    )

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
  def scale(multiplier: Float): Polygon = {
    Polygon(points.map(_ * multiplier))
  }
  @targetName("scaleShorthand")
  def *(value: Float): Polygon = scale(value)
  @targetName("inverseScaleShorthand")
  def /(value: Float): Polygon = *(1f / value)

  /** Returns true if `point` is inside of polygon (excluding the polygon
    * outline).
    */
  override def contains(point: Vector2): Boolean = {
    polygonIntersection.testPoint(point.x, point.y)
  }

  /** Returns true when both points of `line` are inside this polygon (excluding
    * polygon outline)
    */
  def contains(line: Line): Boolean = {
    contains(line.a) && contains(line.b)
  }

  /** Moves each point of the polygon along its normal by the given amount.
    *
    * @return
    *   the grown polygon
    */
  override def grow(amount: Float): Polygon = {

    def offsetVert(
        p1: Vector2,
        p2: Vector2,
        p3: Vector2
    ): Vector2 = {
      val edge1: Vector2 = if isClockwise then p3 - p2 else p2 - p1
      val edge2: Vector2 = if isClockwise then p2 - p1 else p3 - p2
      val halfEdgeAngle: Float = edge1.angleBetween(edge2) / 2f
      val l: Float = amount / sin(halfEdgeAngle)
      val a =
        if isClockwise
        then edge1.angle + pi - halfEdgeAngle
        else edge2.angle + pi + halfEdgeAngle
      val offset = Vector2.fromAngle(a, l)
      val newPoint: Vector2 =
        p2 + offset
      newPoint
    }

    val k = points.size - 1
    val gen = for (i <- 0 to k) yield {
      val p1 = points(if i == 0 then k else i - 1)
      val p2 = points(i)
      val p3 = points(if i == k then 0 else i + 1)
      offsetVert(p1, p2, p3)
    }
    Polygon(gen.toVector)
  }

  // todo: add method mentioned in disclaimer
  /** Checks whether the points of this polygon are near equal to the points of
    * another polygon.
    *
    * @param other
    *   The polygon being compared to this one
    * @param epsilon
    *   Discrepency allowed between points, e.g. 0.40 epsilon means 1.37 is
    *   considered equal to 1.0 is considered equal to 1.00
    * @return
    */
  def nearEquals(other: Polygon, epsilon: Float = 0.0001f): Boolean = {
    // Find the closest point to this polygon's starting point and save its index
    val closestPointIndex = other.points.zipWithIndex.foldLeft(0) {
      case (acc, (point, index)) =>
        if (
          points(0).distanceToSquared(point) < points(0).distanceToSquared(other.points(acc))
        )
          index
        else acc
    }

    // Return
    boundary:
      for (i <- points.indices) {
        val thisPoint = points(i)
        val otherPoint = other.points((i + closestPointIndex) % points.size)
        if (!thisPoint.nearEquals(otherPoint, epsilon))
          boundary.break(false)
      }
      true
  }

  private def _isPolygonClockwise: Boolean = {
    val sum = points.zip(points.tail :+ points.head).foldLeft(0f) {
      case (acc, (p1, p2)) =>
        acc + (p2.x - p1.x) * (p2.y + p1.y)
    }
    sum > 0
  }

  private def _hasDuplicates: Boolean = {
    boundary(
      for
        i <- points.indices
        j <- i + 1 until points.size
      do if points(i) == points(j) then boundary.break(true)
    )
    false
  }
}
