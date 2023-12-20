package engine.math.geometry.meshes2d

import engine.math.Vector2
import engine.math.*
import engine.math.shapes.{Polygon, Shape, Rectangle, Circle}

object MeshGenerator2D {
  def shapePerimeter(shape: Shape): Vector[Vector2] = {
    shape match
      case circle: Circle       => circlePerimeter(circle)
      case rectangle: Rectangle => rectanglePerimeter(rectangle)
      case polygon: Polygon     => polygonPerimeter(polygon)
      case _                    => throw new Exception("Unsupported shape")
  }

  def circlePerimeter(circle: Circle, segments: Int = 32): Vector[Vector2] = {
    val angle = 360f / segments
    val halfRadius = circle.radius / 2
    // return
    (0 until segments).map { i =>
      Vector2(
        halfRadius * cos(angle * i),
        halfRadius * sin(angle * i)
      )
    }.toVector
  }

  def rectanglePerimeter(rectangle: Rectangle): Vector[Vector2] = {
    val width = rectangle.width
    val height = rectangle.height
    Vector(
      Vector2(-width / 2, -height / 2),
      Vector2(width / 2, -height / 2),
      Vector2(width / 2, height / 2),
      Vector2(-width / 2, height / 2)
    )
  }

  def polygonPerimeter(polygon: Polygon): Vector[Vector2] = {
    polygon.points
  }
}
