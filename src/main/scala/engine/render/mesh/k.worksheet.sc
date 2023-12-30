import engine.math.shapes.Polygon
import engine.math.*
import engine.math.shapes.Circle
import engine.render.mesh.Mesh

val polygon = Polygon(
  Vector(
    Vector2(0, 0),
    Vector2(0, 1.3),
    Vector2(1, 1),
    Vector2(1, 0),
    Vector2(0.5f, 0.5f),
    Vector2(0.5f, 0.75f),
    Vector2(0.75f, 0.75f),
    Vector2(0.75f, 0.5f),
    Vector2(0.25f, 0.5f),
    Vector2(0.25f, 0.25f),
    Vector2(0.5f, 0.25f),
    Vector2(0.5f, 0.0f),
    Vector2(0.0f, 0.0f)
  )
)

val circle = Circle(0.5f)
Mesh(circle, 7).vertices.toList

/*
(0.0, 0.0, 0.15587245, 0.19545788, -0.055630237, 0.24373198, -0.22524223, 0.1084709, -0.22524221, -0.10847094, -0.05563025, -0.24373198, 0.15587251, -0.19545783, 0.25, 4.371139E-8, 0.15587243, 0.19545788)
 */
val pts = Vector(
  Vector2(0, 0),
  Vector2(1.55, 1.95),
  Vector2(-0.556, 2.437),
  Vector2(-2.252, 1.084),
  Vector2(-2.252, -1.084),
  Vector2(-0.556, -2.437),
  Vector2(1.55, -1.95),
  Vector2(2.5, 0),
  Vector2(1.55, 1.95)
)

val vertices = polygon.points
  .foldLeft(Array[Float]())((acc, point) =>
    acc ++ Array[Float](point.x, point.y)
  )

val indices = (1 to polygon.points.length)
  .foldLeft(Array[Int]())((acc, i) =>
    acc ++ Array[Int](0, i, if (i == polygon.points.length) 1 else i + 1)
  )

vertices.length
indices.length

vertices.foreach(println)
indices.foreach(println)
