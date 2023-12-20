package engine.math.geometry

import engine.math._

case class Rectangle(width: Float, height: Float) extends Shape2D {

  override val vertices: Vector[Vector2] = {
    Vector(
      Vector2(-width / 2, -height / 2),
      Vector2(width / 2, -height / 2),
      Vector2(width / 2, height / 2),
      Vector2(-width / 2, height / 2)
    )
  }

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

object Rectangle {
  def apply(width: Float, height: Float): Rectangle =
    new Rectangle(width, height)
  def from(vector: Vector2): Rectangle = new Rectangle(vector.x, vector.y)
}
