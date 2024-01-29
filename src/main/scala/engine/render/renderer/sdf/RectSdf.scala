package engine.render.renderer.sdf

import engine.render.renderer.render_element.RenderElement
import engine.render.renderer.RenderData
import engine.math.Matrix3

final case class RectSdf private () extends RenderElement, Bordered {

  private var _width: Float = 0
  private var _height: Float = 0
  private var _cornerRadius: Float = 0

  def width: Float = _width
  def width_=(value: Float): Unit =
    require(value >= 0, "'width' must be >= 0")
    _width = value

  def height: Float = _height
  def height_=(value: Float): Unit =
    require(value >= 0, "'height' must be >= 0")
    _height = value

  def cornerRadius: Float = _cornerRadius
  def cornerRadius_=(value: Float): Unit =
    require(value >= 0, "'cornerRadius' must be >= 0")
    _cornerRadius = value

  override def renderData: RenderData = {
    RenderData(
      vertices = Array[Float](
        -width / 2,
        -height / 2,
        width / 2,
        -height / 2,
        width / 2,
        height / 2,
        -width / 2,
        height / 2
      ),
      indices = Array[Int](
        0, 1, 2, 2, 3, 0
      ),
      layer = layer,
      color = color,
      transform = Matrix3.transform(
        position,
        rotation,
        scale
      ),
      extraUniforms = Map(
        "u_width" -> width,
        "u_height" -> height
      )
    )
  }

}

object RectSdf {
  def apply(width: Float, height: Float): RectSdf =
    val rectSdf = RectSdf()
    rectSdf.width = width
    rectSdf.height = height
    rectSdf
}