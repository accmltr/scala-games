package engine.render.renderer.render_element

import engine.math.{pi, cos, sin}
import engine.render.renderer.RenderData
import engine.render.Color
import engine.math.Matrix3

final case class NGonRenderElement(
    radius: Float,
    segments: Int = 64,
    var layer: Float = 0,
    var color: Color = Color.WHITE,
    var transform: Matrix3 = Matrix3.IDENTITY
) extends RenderElement {

  // Throw exceptions if arguments are invalid
  if (segments < 3)
    throw new IllegalArgumentException("'segments' must be at least 3")

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
      transform = transform
    )
  }

}

object NGonRenderElement {

  private def vertsAndIndicesFromNGon(
      radius: Float,
      segments: Int
  ): (Array[Float], Array[Int]) = {
    val angle = (2 * pi) / segments
    val halfRadius = radius / 2

    val vertices = (0 to segments + 1)
      .foldLeft(Array[Float]())((acc, i) =>
        acc ++ Array[Float](
          if (i == 0) 0 else halfRadius * cos(angle * i),
          if (i == 0) 0 else halfRadius * sin(angle * i)
        )
      )

    val indices = (1 to segments)
      .foldLeft(Array[Int]())((acc, i) =>
        acc ++ Array[Int](0, i, if (i == segments) 1 else i + 1)
      )

    (vertices, indices)
  }
}
