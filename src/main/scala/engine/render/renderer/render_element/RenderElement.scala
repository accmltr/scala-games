package engine.render.renderer.render_element

import engine.render.renderer.RenderData
import engine.render.Color
import engine.math.Vector2

trait RenderElement {
  private var _layer: Float = 0
  private var _color: Color = Color.WHITE
  private var _position: Vector2 = Vector2.zero
  private var _rotation: Float = 0
  private var _scale: Vector2 = Vector2.one

  def renderData: RenderData

  def layer: Float = _layer
  def layer_=(value: Float): Unit =
    _layer = value

  def color: Color = _color
  def color_=(value: Color): Unit =
    require(value != null, "'color' must not be null")
    _color = value

  def position: Vector2 = _position
  def position_=(value: Vector2): Unit =
    require(value != null, "'position' must not be null")
    _position = value

  def rotation: Float = _rotation
  def rotation_=(value: Float): Unit =
    _rotation = value

  def scale: Vector2 = _scale
  def scale_=(value: Vector2): Unit =
    require(value != null, "'scale' must not be null")
    _scale = value

  /** Shorthand for `position.x`. */
  def x = position.x

  /** Shorthand for `position.x = this.position.copy(x = value)`.
    */
  def x_=(value: Float): Unit = position = position.copy(x = value)

  /** Shorthand for `position.y`. */
  def y = position.y

  /** Shorthand for `position.y = this.position.copy(y = value)`.
    */
  def y_=(value: Float): Unit = position = position.copy(y = value)

}
