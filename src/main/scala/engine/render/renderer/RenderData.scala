package engine.render.renderer

import engine.render.shader.{Uniform, Shader}
import engine.render.Color
import engine.math.Matrix3
import scala.util.boundary, boundary.break
import render_data_util.{indicesFromPolygon, verticesFromPolygon}

/** @param vertices
  *   The vertices passed to OpenGL.
  * @param indices
  *   The indices passed to OpenGL.
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
  */
case class RenderData(
    val vertices: Array[Float],
    val indices: Array[Int],
    val layer: Float = 0,
    val color: Color = Color.WHITE,
    val transform: Matrix3 = Matrix3.IDENTITY,
    val shaderOverride: Shader = null,
    val extraUniforms: Map[String, Uniform] = Map.empty
) {

  /** The default shader used when `shaderOverride` is `None`.
    */
  val defaultShader: Shader = Shader(
    "src/main/scala/engine/render/shaders/vertex/default.vert",
    "src/main/scala/engine/render/shaders/fragment/color_fill.frag"
  )

  final val shader =
    Option(shaderOverride).getOrElse(defaultShader)

  // Exceptions
  if defaultShader == null
  then
    throw new IllegalArgumentException(
      "'defaultShader' cannot be null, make sure to override it with a valid shader when extending the RenderData class"
    )
  if vertices == null
  then throw new IllegalArgumentException("'vertices' cannot be null")
  if indices == null
  then throw new IllegalArgumentException("'indices' cannot be null")
  if layer.isNaN
  then throw new IllegalArgumentException("'layer' cannot be NaN")
  if color == null
  then throw new IllegalArgumentException("'color' cannot be null")
  if transform == null
  then throw new IllegalArgumentException("'transform' cannot be null")
  if vertices.length < 3
  then throw new IllegalArgumentException("At least 3 vertices are required")
  if indices.length < 3
  then throw new IllegalArgumentException("At least 3 indices are required")
  for u <- BuiltInUniforms.values
  do
    if extraUniforms.contains(s"$u")
    then
      throw new IllegalArgumentException(
        s"Uniform name '$u' is reserved"
      )

}

object RenderData {
  import engine.math.shapes.*

  /** @param polygon
    *   The polygon to create the vertices and indices from for the render data.
    * @param layer
    * @param color
    * @param transform
    * @param shaderOverride
    *   Optionally used to override the `RenderData.defaultShader` property. (In
    *   this case it uses the `color_fill` shader as the default shader)
    * @param extraUniforms
    *   These are uniforms passed to the shader in addition to the default
    *   uniforms provided by the renderer.
    * @return
    *   A new `RenderData` instance with the vertices and indices created from
    *   the provided polygon.
    */
  def fromPolygon(
      polygon: Polygon,
      extraUniforms: Map[String, Uniform] = Map.empty,
      layer: Float = 0,
      color: Color = Color.WHITE,
      transform: Matrix3 = Matrix3.IDENTITY,
      shaderOverride: Shader = null
  ): RenderData = {
    // Exceptions
    if polygon == null
    then throw new IllegalArgumentException("'polygon' cannot be null")
    // Result
    new RenderData(
      vertices = verticesFromPolygon(polygon),
      indices = indicesFromPolygon(polygon),
      layer = layer,
      color = color,
      transform = transform,
      shaderOverride = shaderOverride,
      extraUniforms = extraUniforms
    )
  }
}
