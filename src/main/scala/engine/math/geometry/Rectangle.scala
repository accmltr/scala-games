package engine.math.geometry

import engine.math._

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
