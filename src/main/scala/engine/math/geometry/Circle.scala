package engine.math.geometry

import engine.math._

case class Circle(radius: Float, segments: Int) extends Shape2D {
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
    val angle = 360f / segments
    val halfRadius = radius / 2
    Polygon(
      (0 until segments).map { i =>
        Vector2(
          halfRadius * cos(angle * i),
          halfRadius * sin(angle * i)
        )
      }.toVector
    )
  }
}

object Circle {
  def apply(radius: Float): Circle = {
    new Circle(radius, 32)
  }
}
