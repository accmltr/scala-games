package engine.render.renderer.render_data

import engine.math.*
import engine.math.shapes.NGon

private[render_data] def vertsAndIndicesFromNGon(
    ngon: NGon
): (Array[Float], Array[Int]) = {
  val angle = (2 * pi) / ngon.segments
  val halfRadius = ngon.radius / 2

  val vertices = (0 to ngon.segments + 1)
    .foldLeft(Array[Float]())((acc, i) =>
      acc ++ Array[Float](
        if (i == 0) 0 else halfRadius * cos(angle * i),
        if (i == 0) 0 else halfRadius * sin(angle * i)
      )
    )

  val indices = (1 to ngon.segments)
    .foldLeft(Array[Int]())((acc, i) =>
      acc ++ Array[Int](0, i, if (i == ngon.segments) 1 else i + 1)
    )

  (vertices, indices)
}
