package engine.render.renderer.sdf

import engine.render.Color
import engine.math.Matrix3
import engine.render.renderer.render_element.RenderElement
import engine.math.Vector2
import engine.render.renderer.RenderData
import engine.render.shader.Shader
import engine.math.Vector4

final case class CircleSdf(
    var radius: Float,
    var borderInnerWidth: Float = 0,
    var borderOuterWidth: Float = 0,
    var borderColor: Color = Color.BLACK,
    var layer: Float = 0,
    var color: Color,
    var position: Vector2 = Vector2.zero,
    var rotation: Float = 0,
    var scale: Vector2 = Vector2.one
) extends RenderElement {

  /** Shorthand for `borderInnerWidth`.
    */
  def biw: Float = borderInnerWidth
  def biw_=(value: Float): Unit = borderInnerWidth = value

  /** Shorthand for `borderOuterWidth`.
    */
  def bow: Float = borderOuterWidth
  def bow_=(value: Float): Unit = borderOuterWidth = value

  override def renderData: RenderData = {
    val totalRadius = radius + borderOuterWidth
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
        "uRadius" -> radius,
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
