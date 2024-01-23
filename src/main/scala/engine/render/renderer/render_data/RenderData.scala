package engine.render.renderer.render_data

import engine.render.shader.{Uniform, Shader}
import engine.render.Color
import engine.math.Matrix3
import scala.util.boundary, boundary.break
import engine.math.geometry.Polyline

/** @param layer
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
trait RenderData(
    shader: Shader = Shader(
      "src/main/scala/engine/render/shaders/vertex/default.vert",
      "src/main/scala/engine/render/shaders/fragment/color_fill.frag"
    ),
    vertices: Array[Float] = null,
    indices: Array[Int] = null,
    layer: Float = 0,
    color: Color = Color.WHITE,
    transform: Matrix3 = Matrix3.IDENTITY,
    shaderOverride: Shader = null,
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
  if indices == null
  then throw new IllegalArgumentException("'indices' not initialized")
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

// object RenderData {
//   import engine.math.shapes.*

//   /** @param polygon
//     *   The polygon to create the vertices and indices from for the render data.
//     * @param layer
//     *   The layer value determines the order in which objects are drawn, with
//     *   lower values being drawn first. Values include all real numbers, meaning
//     *   that negative values can be used as well.
//     * @param color
//     * @param transform
//     * @param shaderOverride
//     *   Optionally used to override the `RenderData.defaultShader` property. (In
//     *   this case it uses the `color_fill` shader as the default shader)
//     * @param extraUniforms
//     *   These are uniforms passed to the shader in addition to the default
//     *   uniforms provided by the renderer.
//     * @return
//     *   A new `RenderData` instance with the vertices and indices created from
//     *   the provided polygon.
//     */
//   def fromPolygon(
//       polygon: Polygon,
//       layer: Float = 0,
//       color: Color = Color.WHITE,
//       transform: Matrix3 = Matrix3.IDENTITY,
//       shaderOverride: Shader = null,
//       extraUniforms: Map[String, Uniform] = Map.empty
//   ): RenderData = {
