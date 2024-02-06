package engine.render.renderer.sdf

import engine.render.renderer.render_element.RenderElement
import engine.render.renderer.RenderData
import engine.math.Matrix3
import engine.render.shader.Shader

/** Each mode determines how the corners of the border will be rounded.
  *
  * @param value
  *   The integer value of the mode. Passed to the shader.
  */
enum CornerMode(val value: Int):
  /** Outer borders will always have a constant width. Which means that with no
    * corner radius, the outer border corners will have a radius equal to the
    * outer border width.
    *
    * Inner border corners will have a radius equal to the `cornerRadius -
    * borderInnerWidth`. Which means that inner border width will not be
    * constant when `borderInnerWidth > cornerRadius`.
    */
  case ConstantWidth extends CornerMode(0)

  /** Both inner and outer border corners will always have radii equal to
    * `cornerRadius`
    */
  case EquivalentRadii extends CornerMode(1)

  /** When the `cornerRadius > borderOuterWidth`, corners will behave the same
    * as in `ConstantWidth` mode.
    *
    * When `cornerRadius < borderOuterWidth`, the outer border corners will
    * transition from a radius of `borderOuterWidth + cornerRadius` to `0` as
    * `cornerRadius` approaches `0`. Inner border corner behavior remains the
    * same.
    */
  case AutoSharpen extends CornerMode(2)

final case class RectSdf private () extends RenderElement, Bordered {

  private var _width: Float = 0
  private var _height: Float = 0
  private var _cornerRadius: Float = 0
  private var _cornerMode: CornerMode = CornerMode.AutoSharpen

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

    /** Determines behavior of border corners, i.e. determines how they are
      * rounded.
      *
      * Read the docs of the `CornerMode` members for more information.
      */
  def cornerMode: CornerMode = _cornerMode
  def cornerMode_=(value: CornerMode): Unit = _cornerMode = value

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
    val xRadius = width / 2f + borderOuterWidth
    val yRadius = height / 2f + borderOuterWidth
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
        "uCornerRadius" -> cornerRadius,
        "uCornerMode" -> cornerMode.value
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
