package engine.math

trait Shape extends ShapeOps[Shape] {
  def toPolygon: Polygon
}

trait Shape2D extends Shape

case class Rectangle(width: Float, height: Float) extends Shape2D {

  override def translate(translation: Vector2): Shape = {
    Rectangle from (Vector2(width, height) + translation)
  }

  override def rotate(degrees: Float): Shape = {}

  override def grow(amount: Float): Shape = ???

  override def scale(amount: Float): Shape = ???

  override def intersects(other: Shape): Boolean = ???

  override def contains(point: Vector2): Boolean = ???

  override def rotateAround(degrees: Float, point: Vector2): Shape = ???

  override def toPolygon: Polygon = {
    val halfWidth = width / 2
    val halfHeight = height / 2
    Polygon(
      List(
        Vector2(-halfWidth, -halfHeight),
        Vector2(halfWidth, -halfHeight),
        Vector2(halfWidth, halfHeight),
        Vector2(-halfWidth, halfHeight)
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
  def toPolygon: Polygon = this
}

private trait ShapeOps[T <: Shape] {
  def intersects(other: T): Boolean
  def contains(point: Vector2): Boolean
  def grow(amount: Float): T
  def scale(amount: Float): T
  def translate(translation: Vector2): T
  def rotate(degrees: Float): T
  def rotateAround(degrees: Float, point: Vector2): T
}

// Implicit Conversions
object Shape {
  implicit def toPolygon(shape: Shape): Polygon = shape.toPolygon

  implicit def vector2ToRectangle(vector: Vector2): Rectangle =
    Rectangle(vector.x, vector.y)

  implicit def rectangleToVector2(rectangle: Rectangle): Vector2 =
    Vector2(rectangle.width, rectangle.height)
}
