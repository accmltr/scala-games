import engine.math._
import engine.math.geometry.{Rectangle, Polygon}
val rectangle = Rectangle(10, 10)
val polygon = Polygon(
  Vector(Vector2(-5, 5), Vector2(5, 5), Vector2(5, -5), Vector2(-5, -5))
)
rectangle.toPolygon nearEquals polygon

val grownRectangle = Rectangle(10, 10).grow(2).toPolygon
val grownPolygon = Rectangle(10, 10).toPolygon.grow(2)

grownRectangle.toPolygon nearEquals grownPolygon
