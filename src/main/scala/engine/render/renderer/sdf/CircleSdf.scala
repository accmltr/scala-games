package engine.render.renderer.sdf

import engine.render.renderer.render_data.RenderData
import engine.render.Color
import engine.math.Matrix3

final case class CircleSdf(
    var radius: Float,
    var border: Float,
    var layer: Float = 0,
    var color: Color,
    var transform: Matrix3 = Matrix3.IDENTITY
) extends RenderData(
      ViewportQuad.vertices,
      ViewportQuad.indices,
      0,
      color,
      transform
    ) {
  override def toString: String =
    s"CircleSdf(radius=$radius, border=$border, color=$color, transform=$transform)"
}
