package engine.render.renderer

import engine.render.shader.{Uniform, Shader}
import engine.render.Color
import engine.math.Matrix3
import scala.util.boundary, boundary.break
import engine.math.geometry.Polyline

/** The RenderData class serves as an immutable snapshot of the data needed to
  * render an object. Mutable game objects are need to be passed to the renderer
  * by creating a snapshots of their current state in the form of a RenderData
  * object. This makes maintainability and safety of the Renderer class much
  * easier.
  *
  * @param layer
  *   The layer value determines the order in which objects are drawn, with
  *   lower values being drawn first.
  * @param color
  *   The value of the 'color' uniform.
  * @param transform
  *   The transform matrix passed to the shader.
  * @param shaderOverride
  *   Optionally used to override the `RenderData.defaultShader` property.
  * @param extraUniforms
  *   These are uniforms passed to the shader in addition to the default
  *   uniforms provided by the renderer.
  * @param shader
  */
final private[render] case class RenderData(
    shader: Shader = Shader(
      "src/main/scala/engine/render/shaders/vertex/default.vert",
      "src/main/scala/engine/render/shaders/fragment/color_fill.frag"
    ),
    vertices: Array[Float] = null,
    indices: Array[Int] = null,
    layer: Float = 0,
    color: Color = Color.WHITE,
    transform: Matrix3 = Matrix3.IDENTITY,
    extraUniforms: Map[String, Uniform] = Map.empty
) {
  // Throw exceptions if arguments are invalid
  if shader == null
  then
    throw new IllegalArgumentException(
      "'shader' cannot be null"
    )
  if vertices == null
  then throw new IllegalArgumentException("'vertices' not initialized")
  if vertices.size < 3
  then
    throw new IllegalArgumentException(
      "'vertices' must have at least 3 elements"
    )
  if indices == null
  then throw new IllegalArgumentException("'indices' not initialized")
  if indices.size < 3
  then
    throw new IllegalArgumentException(
      "'indices' must have at least 3 elements"
    )
  if layer.isNaN
  then throw new IllegalArgumentException("'layer' cannot be NaN")
  if color == null
  then throw new IllegalArgumentException("'color' cannot be null")
  if transform == null
  then throw new IllegalArgumentException("'transform' cannot be null")
  if extraUniforms == null
  then throw new IllegalArgumentException("'extraUniforms' cannot be null")
  for u <- engine.render.renderer.BuiltInUniforms.values
  do
    if extraUniforms.contains(s"$u")
    then
      throw new IllegalArgumentException(
        s"Uniform name '$u' is reserved"
      )
}
