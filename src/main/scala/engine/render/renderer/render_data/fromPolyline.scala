package engine.render.renderer.render_data

import engine.math.geometry.{Polyline, Line}
import engine.math.Vector2

private[render_data] def vertsAndIndicesFromPolyline(
    polyline: Polyline,
    width: Float
): (Array[Float], Array[Int]) = {
  def vertices(points: Array[Vector2], width: Float): Array[Float] = {
    (for i <- 0 until points.length - 1
    yield
      // Note: View 'p' to 'p + 1' as the forwards direction.
      val line = Line(points(i), points(i + 1))
      val offset = line.normal * width * 0.5f
      val p1 = line.a + offset // bottom left
      val p2 = line.a - offset // bottom right
      val p3 = line.b + offset // top left
      val p4 = line.b - offset // top right
      Array(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
    ).flatten.toArray
  }

  def indices(pointCount: Int): Array[Int] = {
    (0 until 4 * (pointCount - 1) by 4).foldLeft(Array[Int]())((acc, i) =>
      acc ++ Array(i, i + 1, i + 2, i + 2, i + 1, i + 3)
    )
  }
  (vertices(polyline.points.toArray, width), indices(polyline.count))
}
