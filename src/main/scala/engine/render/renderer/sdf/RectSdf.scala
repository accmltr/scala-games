package engine.render.renderer.sdf

import engine.render.renderer.render_element.RenderElement
import engine.render.renderer.RenderData
import engine.math.Matrix3
import engine.render.shader.Shader

final case class RectSdf private () extends RenderElement, Bordered {

  private var _width: Float = 0
  private var _height: Float = 0
  private var _cornerRadius: Float = 0
  private var _constantBorderWidth: Boolean = false
  private var _autoCBW: Boolean = false

  def width: Float = _width
  def width_=(value: Float): Unit =
    require(value >= 0, "'width' must be >= 0")
    require(value >= cornerRadius * 2, "'width' must be >= 'cornerRadius' * 2")
    _width = value

  def height: Float = _height
  def height_=(value: Float): Unit =
    require(value >= 0, "'height' must be >= 0")
    require(value >= cornerRadius * 2, "'height' must be >= 'cornerRadius' * 2")
    _height = value

  def cornerRadius: Float = _cornerRadius
  def cornerRadius_=(value: Float): Unit =
    require(value >= 0, "'cornerRadius' must be >= 0")
    require(value <= width / 2, "'cornerRadius' must be <= 'width' / 2")
    require(value <= height / 2, "'cornerRadius' must be <= 'height' / 2")
    _cornerRadius = value

    /** If `true`, the border will maintain a constant with around the corners
      * of the rect by rounding the outer corner around the center of the inner
      * corner. If `false`, the inner and outer radii of the border will be
      * equal, meaning the border will not maintain a constant width around the
      * corners of the rect.
      *
      * This is analogous to the `equivalentCornerRadii` property.
      *
      * @return
      */
  def constantBorderWidth: Boolean = _constantBorderWidth
  def constantBorderWidth_=(value: Boolean): Unit =
    _constantBorderWidth = value

    /** If `true`, `constandBorderWidth` will be set to `false` when
      * `cornerRadius` is set to a value smaller than the width of the outer
      * border. If `false`, `equivalentCornerRadii` will not be automatically
      * set, and a rect with no corner radius will have a outer border with a
      * rounding equal in radius to the width of the outer border.
      *
      * The default value is `false`.
      *
      * @return
      */
  def autoCBW: Boolean = _autoCBW
  def autoCBW_=(value: Boolean): Unit =
    _autoCBW = value

  def maxCornerRadius: Float =
    if width < height then width / 2 else height / 2

  /** Shorthand for `width`. */
  def w = width

  /** Shorthand for `width =`. */
  def w_=(value: Float): Unit = width = value

  /** Shorthand for `height`. */
  def h = height

  /** Shorthand for `height =`. */
  def h_=(value: Float): Unit = height = value

  /** Shorthand for `cornerRadius`. */
  def cr = cornerRadius

  /** Shorthand for `cornerRadius =`. */
  def cr_=(value: Float): Unit = cornerRadius = value

  /** Shorthand for `maxCornerRadius`. */
  def mcr = maxCornerRadius

  override def renderData: RenderData = {
    val xRadius = width + borderOuterWidth
    val yRadius = height + borderOuterWidth
    RenderData(
      shader = Shader(
        "src/main/scala/engine/render/shaders/vertex/default.vert",
        "src/main/scala/engine/render/shaders/fragment/rect_sdf.frag"
      ),
      vertices = Array[Float](
        -xRadius,
        -yRadius,
        xRadius,
        -yRadius,
        xRadius,
        yRadius,
        -xRadius,
        yRadius
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
        "uWidth" -> width,
        "uHeight" -> height,
        "uCornerRadius" -> cornerRadius
      ) ++ borderUniforms // TODO: streamline uniform additions
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
