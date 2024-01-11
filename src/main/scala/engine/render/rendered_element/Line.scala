package engine.render.rendered_element

import engine.render.render_manager.RenderManager
import engine.render.Color
import engine.render.shader_classes.Shader
import engine.math.Matrix3
import engine.render.mesh.Mesh
import engine.render.render_manager
import engine.math.Vector2
import engine.math.geometry.Line as geoLine

final case class Line(
    val points: Array[Vector2],
    val width: Float = 1,
    override val shader: Shader,
    override val transform: Matrix3 = Matrix3.IDENTITY,
    override val layer: Float = 0,
    override val tint: Color = Color.WHITE,
    override val uniforms: Map[String, Uniforms] = Map.empty
) extends RenderedElement {

  override def newManager: RenderManager = render_manager.Line()

  override def isManager(manager: RenderManager): Boolean =
    manager match
      case _: render_manager.Line => true
      case _                      => false

  def vertices: Array[Float] = Line.pointsToRectVerts(points, width)

  def indices: Array[Int] = Line.rectTriIndices(points.size)

}

object Line {
  def pointsToRectVerts(points: Array[Vector2], width: Float): Array[Float] = {
    (for i <- 0 until points.length - 1
    yield
      val line = geoLine(points(i), points(i + 1))
      val offset = line.normal * width * 0.5f
      val p1 = line.a + offset // bottom left
      val p2 = line.a - offset // bottom right
      val p3 = line.b - offset // top left
      val p4 = line.b + offset // top right
      // View 'p' to 'p + 1' as the forwards direction:
      Array(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
    ).flatten.toArray
  }

  def rectTriIndices(pointCount: Int): Array[Int] = {
    (0 until pointCount - 1).foldLeft(Array[Int]())((acc, i) =>
      acc ++ Array(i, i + 1, i + 3, i + 3, i + 1, i + 2)
    )
  }

}
