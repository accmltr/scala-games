package engine.render.renderer.sdf

object ViewportQuad {
  val vertices = Array[Float](
    -1, -1, 1, -1, 1, 1, -1, 1
  )
  val indices = Array[Int](
    0, 1, 2, 2, 3, 0
  )

}
