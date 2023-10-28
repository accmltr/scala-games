package engine.math.geometry

import engine.math._
import engine.math.Operations.pow

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
