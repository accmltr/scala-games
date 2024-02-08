package engine.render.renderer.render_element

import engine.render.Image
import engine.render.renderer.RenderData
import engine.math.Matrix3

final case class Sprite private (private val image: Image)
    extends RenderElement {

  override def renderData: RenderData = {
    RenderData(
      vertices = Sprite.vertices,
      indices = Sprite.indices,
      layer = layer,
      color = color,
      transform = Matrix3.transform(
        position,
        rotation,
        scale
      )
      // extraUniforms = Map("uTexture" -> image)
    )
  }
}

object Sprite {
  val vertices: Array[Float] = Array(
    -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f
  )
  val indices: Array[Int] = Array(0, 1, 2, 2, 3, 0)

  def apply(path: String): Sprite = new Sprite(Image(path))
  def apply(image: Image): Sprite = new Sprite(image)
}
