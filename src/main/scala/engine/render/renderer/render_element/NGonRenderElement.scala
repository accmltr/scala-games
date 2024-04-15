package engine.render.renderer.render_element

import engine.math.{pi, cos, sin}
import engine.render.renderer.RenderData
import engine.render.Color
import engine.math.Matrix3
import engine.math.Vector2

final case class NGonRenderElement private () extends RenderElement {

  private var _radius: Float = 0
  private var _segments: Int = 3

  def radius: Float = _radius
  def radius_=(value: Float): Unit =
    require(value >= 0, "'radius' must be >= 0")
    _radius = value

  def segments: Int = _segments
  def segments_=(value: Int): Unit =
    require(value >= 3, "'segments' must be >= 3")
    _segments = value

  override def renderData: RenderData = {
    val (verts, indices) = NGonRenderElement.vertsAndIndicesFromNGon(
      radius,
      segments
    )
    RenderData(
      vertices = verts,
      indices = indices,
      layer = layer,
      color = color,
      transform = Matrix3(
        position,
        rotation,
        scale
      )
    )
  }

}

object NGonRenderElement {

  def apply(radius: Float, segments: Int = 32): NGonRenderElement =
    val ngonRe = NGonRenderElement()
    ngonRe.radius = radius
    ngonRe.segments = segments
    ngonRe

  private def vertsAndIndicesFromNGon(
      radius: Float,
      segments: Int
  ): (Array[Float], Array[Int]) = {
    val angle = (2 * pi) / segments

    val vertices = (0 to segments + 1)
      .foldLeft(Array[Float]())((acc, i) =>
        acc ++ Array[Float](
          if (i == 0) 0 else radius * cos(angle * i),
          if (i == 0) 0 else radius * sin(angle * i)
        )
      )

    val indices = (1 to segments)
      .foldLeft(Array[Int]())((acc, i) =>
        acc ++ Array[Int](0, i, if (i == segments) 1 else i + 1)
      )

    (vertices, indices)
  }
}
