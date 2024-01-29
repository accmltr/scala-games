package engine.render.renderer.sdf

import engine.render.Color
import engine.math.Matrix3
import engine.render.renderer.render_element.RenderElement
import engine.math.Vector2
import engine.render.renderer.RenderData
import engine.render.shader.Shader
import engine.math.Vector4
import engine.math.abs

object CircleSdf {
  def apply(
      radius: Float = 0,
      borderInnerWidth: Float = 0,
      borderOuterWidth: Float = 0,
      borderColor: Color = Color.BLACK
  ): CircleSdf = {
    val circle = CircleSdf()
    circle.radius = radius
    circle.borderInnerWidth = borderInnerWidth
    circle.borderOuterWidth = borderOuterWidth
    circle.borderColor = borderColor
    circle
  }
}

/** @param radius
  * @param borderInnerWidth
  *   The width of the inner border. Must be >= 0. This parameter is hidden
  *   after construction, use `borderInnerWidth` instead.
  * @param borderOuterWidth
  *   The width of the outer border. Must be >= 0. This parameter is hidden
  *   after construction, use `borderOuterWidth` instead.
  * @param borderColor
  * @param layer
  * @param color
  * @param position
  * @param rotation
  * @param scale
  */
final case class CircleSdf private () extends RenderElement {

  private var _radius: Float = 0
  private var _borderInnerWidth: Float = 0
  private var _borderOuterWidth: Float = 0
  private var _borderColor: Color = Color.BLACK

  /* The following setters and getters enforce constraints on some of the class's properties.
   * This pattern is not pleasant to look at, but has the advantage of throwing exceptions
   * at the point of assignment, rather than at a later point, when the properties cause a problem elsewhere.
   */

  def radius: Float = _radius
  def radius_=(value: Float): Unit = {
    require(value >= 0, "radius must be >= 0")
    _radius = value
  }

  def borderInnerWidth: Float = _borderInnerWidth
  def borderInnerWidth_=(value: Float): Unit = {
    require(value >= 0, "borderInnerWidth must be >= 0")
    _borderInnerWidth = value
  }

  def borderOuterWidth: Float = _borderOuterWidth
  def borderOuterWidth_=(value: Float): Unit = {
    require(value >= 0, "borderOuterWidth must be >= 0")
    _borderOuterWidth = value
  }

  def borderColor: Color = _borderColor
  def borderColor_=(value: Color): Unit = {
    require(value != null, "'borderColor' must not be null")
    _borderColor = value
  }

  /** Shorthand for `borderInnerWidth`.
    */
  def biw: Float = borderInnerWidth
  def biw_=(value: Float): Unit = borderInnerWidth = value

  /** Shorthand for `borderOuterWidth`.
    */
  def bow: Float = borderOuterWidth
  def bow_=(value: Float): Unit = borderOuterWidth = value

  override def renderData: RenderData = {
    val totalRadius = abs(radius) + borderOuterWidth
    RenderData(
      shader = Shader(
        "src/main/scala/engine/render/shaders/vertex/default_with_interp_pos.vert",
        "src/main/scala/engine/render/shaders/fragment/circle_sdf.frag"
      ),
      vertices = Array[Float](
        -totalRadius,
        -totalRadius,
        totalRadius,
        -totalRadius,
        totalRadius,
        totalRadius,
        -totalRadius,
        totalRadius
      ),
      indices = Array[Int](0, 1, 2, 2, 3, 0),
      layer = layer,
      color = color,
      transform = Matrix3.transform(
        position,
        rotation,
        scale
      ),
      extraUniforms = Map(
        "uRadius" -> abs(radius),
        "uBorderInnerWidth" -> borderInnerWidth,
        "uBorderOuterWidth" -> borderOuterWidth,
        "uBorderColor" -> Vector4(
          borderColor.r,
          borderColor.g,
          borderColor.b,
          borderColor.a
        )
      )
    )
  }
}
