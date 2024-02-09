package engine.render.renderer.render_element

import engine.render.Image
import engine.render.renderer.RenderData
import engine.math.Matrix3
import engine.render.shader.Shader

final case class Sprite private (private val image: Image)
    extends RenderElement {

  override def renderData: RenderData = {
    val vertices: Array[Float] = Array(
      image.width,
      image.height,
      0,
      image.height,
      0,
      0,
      image.width,
      0
    )
    val indices: Array[Int] = Array(0, 1, 2, 2, 3, 0)
    RenderData(
      shader = Shader(
        "src/main/scala/engine/render/shaders/vertex/default.vert",
        "src/main/scala/engine/render/shaders/fragment/sprite.frag"
      ),
      vertices = vertices,
      indices = indices,
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
  def apply(path: String): Sprite = new Sprite(Image(path))
  def apply(image: Image): Sprite = new Sprite(image)
}
