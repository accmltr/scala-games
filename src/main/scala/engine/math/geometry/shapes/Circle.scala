package engine.math.geometry.shapes

import engine.math._

case class Circle(radius: Float) extends Shape2D {

  lazy val radiusSquared = pow(radius, 2)

  override def contains(point: Vector2): Boolean = {
    point.lengthSquared <= radiusSquared
  }
  override def scale(amount: Float): Circle = {
    Circle(radius * amount)
  }
  override def grow(amount: Float): Circle = {
    Circle(radius + amount)
  }
}
