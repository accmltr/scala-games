package engine.render.renderer.sdf

import engine.render.Color
import engine.render.renderer.render_element.RenderElement

trait Bordered {
  private var _borderInnerWidth: Float = 0
  private var _borderOuterWidth: Float = 0
  private var _borderColor: Color = Color.BLACK

  def borderInnerWidth: Float = _borderInnerWidth
  def borderInnerWidth_=(value: Float): Unit =
    require(value >= 0, "'borderInnerWidth' must be >= 0")
    _borderInnerWidth = value

  def borderOuterWidth: Float = _borderOuterWidth
  def borderOuterWidth_=(value: Float): Unit =
    require(value >= 0, "'borderOuterWidth' must be >= 0")
    _borderOuterWidth = value

  def borderColor: Color = _borderColor
  def borderColor_=(value: Color): Unit =
    require(value != null, "'borderColor' must not be null")
    _borderColor = value

  /** Shorthand for `borderInnerWidth`.
    */
  def biw: Float = borderInnerWidth

  /** Shorthand for `borderInnerWidth =`.
    */
  def biw_=(value: Float): Unit = borderInnerWidth = value

  /** Shorthand for `borderOuterWidth`.
    */
  def bow: Float = borderOuterWidth

  /** Shorthand for `borderOuterWidth =`.
    */
  def bow_=(value: Float): Unit = borderOuterWidth = value
}
