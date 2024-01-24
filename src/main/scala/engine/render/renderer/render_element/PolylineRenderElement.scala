package engine.render.renderer.render_element

import engine.math.{pi, cos, sin}
import engine.render.renderer.RenderData
import engine.render.Color
import engine.math.Matrix3
import engine.math.geometry.Polyline
import engine.math.Vector2
import engine.math.geometry.Line

final case class PolylineRenderElement(
    points: List[Vector2],
    width: Float = 1,
    var layer: Float = 0,
    var color: Color = Color.WHITE,
    var transform: Matrix3 = Matrix3.IDENTITY
) extends RenderElement {

  // Throw exceptions if arguments are invalid
  if points == null
  then throw new IllegalArgumentException("'points' not initialized")
  if points.size < 2
  then
    throw new IllegalArgumentException(
      "'points' must have at least 2 elements"
    )

  override def renderData: RenderData = {
    val (verts, indices) =
      PolylineRenderElement.vertsAndIndicesFromPolyline(points, width)
    RenderData(
      vertices = verts,
      indices = indices,
      layer = layer,
      color = color,
      transform = transform
    )
  }

}

object PolylineRenderElement {
  private def vertsAndIndicesFromPolyline(
      points: List[Vector2],
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
    (vertices(points.toArray, width), indices(points.length))
  }
}
