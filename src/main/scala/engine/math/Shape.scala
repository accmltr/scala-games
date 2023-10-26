package engine.math

import engine.math.Operations.pow
import org.joml
import scala.compiletime.ops.float
import engine.math.Constants.pi

trait Shape extends ShapeOps[Shape] {
  def toPolygon: Polygon
}

trait Shape2D extends Shape

case class Rectangle(width: Float, height: Float) extends Shape2D {
  override def contains(point: Vector2): Boolean = {
    val halfWidth = width / 2
    val halfHeight = height / 2
    point.x >= -halfWidth && point.x <= halfWidth &&
    point.y >= -halfHeight && point.y <= halfHeight
  }
  override def scale(amount: Float): Rectangle = {
    Rectangle(width * amount, height * amount)
  }
  override def grow(amount: Float): Rectangle = {
    Rectangle(width + 2 * amount, height + 2 * amount)
  }

  override def toPolygon: Polygon = {
    val halfWidth = width / 2
    val halfHeight = height / 2
    Polygon(
      List(
        Vector2(-halfWidth, halfHeight),
        Vector2(halfWidth, halfHeight),
        Vector2(halfWidth, -halfHeight),
        Vector2(-halfWidth, -halfHeight)
      )
    )
  }

}

object Rectangle {
  def apply(width: Float, height: Float): Rectangle =
    new Rectangle(width, height)
  def from(vector: Vector2): Rectangle = new Rectangle(vector.x, vector.y)
}

case class Circle(radius: Float) extends Shape2D {
  val radiusSquared = pow(radius, 2)
  override def contains(point: Vector2): Boolean = {
    point.lengthSquared <= radiusSquared
  }
  override def scale(amount: Float): Circle = {
    Circle(radius * amount)
  }
  override def grow(amount: Float): Circle = {
    Circle(radius + amount)
  }
  def toPolygon: Polygon = {
    val points = 32
    val angle = 360f / points
    val halfRadius = radius / 2
    Polygon(
      (0 until points).map { i =>
        Vector2(
          halfRadius * Operations.cos(angle * i),
          halfRadius * Operations.sin(angle * i)
        )
      }.toList
    )
  }
}

case class Polygon(points: List[Vector2]) extends Shape2D {

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

    def _aux(
        remaining: List[Vector2],
        previous: Vector2 = points.lastOption.getOrElse(Vector2.zero)
    ): List[Vector2] = {
      remaining match {
        case Nil => Nil
        case head :: next =>
          val nextPoint = next.headOption.getOrElse(points.head)
          if isClockwise then {
            val edge1: Vector2 = head - previous
            val edge2: Vector2 = nextPoint - head
            val halfEdgeAngle: Float = edge1.angleBetween(edge2) / 2f
            val l: Float = amount / Operations.sin(halfEdgeAngle)
            val a = edge1.angle + halfEdgeAngle
            val newPoint: Vector2 =
              head + Vector2.fromAngle(a, l)
            newPoint :: _aux(next, head)
          } else {
            val edge1: Vector2 = nextPoint - head
            val edge2: Vector2 = head - previous
            val halfEdgeAngle: Float = edge1.angleBetween(edge2) / 2f
            val l: Float = amount / Operations.sin(halfEdgeAngle)
            val a = edge1.angle + pi + halfEdgeAngle
            val newPoint: Vector2 =
              head + Vector2.fromAngle(a, l)
            newPoint :: _aux(next, head)
          }
      }
    }

    Polygon(_aux(points))
  }
  def toPolygon: Polygon = this

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

private trait ShapeOps[T <: Shape] {

  /** @param point
    *   a point relative to the shape origin
    * @return
    */
  def contains(point: Vector2): Boolean

  /** @param amount
    * @return
    */
  def grow(amount: Float): T
  def scale(amount: Float): T
}

// Implicit Conversions
object Shape {
  implicit def toPolygon(shape: Shape): Polygon = shape.toPolygon

  implicit def vector2ToRectangle(vector: Vector2): Rectangle =
    Rectangle(vector.x, vector.y)

  implicit def rectangleToVector2(rectangle: Rectangle): Vector2 =
    Vector2(rectangle.width, rectangle.height)
}
