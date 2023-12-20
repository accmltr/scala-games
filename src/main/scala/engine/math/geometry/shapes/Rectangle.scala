package engine.math.geometry.shapes

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
}
