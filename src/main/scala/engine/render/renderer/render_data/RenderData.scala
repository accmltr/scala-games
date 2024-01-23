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
  * @param defaultShader
  *   The default shader used by this subclass of `RenderData` when
  *   `shaderOverride` is `None`.
  */
trait RenderData(
    val defaultShader: Shader = Shader(
      "src/main/scala/engine/render/shaders/vertex/default.vert",
      "src/main/scala/engine/render/shaders/fragment/color_fill.frag"
    ),
    private var _vertices: Array[Float] = null,
    private var _indices: Array[Int] = null,
    private var _layer: Float = 0,
    private var _color: Color = Color.WHITE,
    private var _transform: Matrix3 = Matrix3.IDENTITY,
    private var _shaderOverride: Shader = null,
    private var _extraUniforms: Map[String, Uniform] = Map.empty
) {

  // Throw exceptions if arguments are invalid
  if defaultShader == null
  then
    throw new IllegalArgumentException(
      "'defaultShader' cannot be null, make sure to override it with a valid shader when extending the RenderData class"
    )
  // Invoke setters to throw exceptions if arguments are invalid
  layer = _layer
  color = _color
  transform = _transform
  shaderOverride = _shaderOverride
  extraUniforms = _extraUniforms

  // Setters and getters

  def vertices: Array[Float] = {
    if _vertices == null
    then throw new IllegalArgumentException("'vertices' not initialized")
    _vertices
  }
  def vertices_=(value: Array[Float]): Unit = {
    _vertices = value
    if vertices == null
    then throw new IllegalArgumentException("'vertices' cannot be null")
    if vertices.length < 3
    then throw new IllegalArgumentException("At least 3 vertices are required")
  }

  def indices: Array[Int] = {
    if _indices == null
    then throw new IllegalArgumentException("'indices' not initialized")
    _indices
  }
  def indices_=(value: Array[Int]): Unit = {
    _indices = value
    if indices == null
    then throw new IllegalArgumentException("'indices' cannot be null")
    if indices.length < 3
    then throw new IllegalArgumentException("At least 3 indices are required")
  }

  def layer: Float = _layer
  def layer_=(value: Float): Unit = {
    _layer = value
    if layer.isNaN
    then throw new IllegalArgumentException("'layer' cannot be NaN")
  }

  def color: Color = _color
  def color_=(value: Color): Unit = {
    _color = value
    if color == null
    then throw new IllegalArgumentException("'color' cannot be null")
  }

  def transform: Matrix3 = _transform
  def transform_=(value: Matrix3): Unit = {
    _transform = value
    if transform == null
    then throw new IllegalArgumentException("'transform' cannot be null")
  }

  def shaderOverride: Shader = _shaderOverride
  def shaderOverride_=(value: Shader): Unit = {
    _shaderOverride = value
  }

  def extraUniforms: Map[String, Uniform] = _extraUniforms
  def extraUniforms_=(value: Map[String, Uniform]): Unit = {
    _extraUniforms = value
    for u <- engine.render.renderer.BuiltInUniforms.values
    do
      if extraUniforms.contains(s"$u")
      then
        throw new IllegalArgumentException(
          s"Uniform name '$u' is reserved"
        )
  }

  def shader =
    Option(shaderOverride).getOrElse(defaultShader)
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
//     // Exceptions
//     if polygon == null
//     then throw new IllegalArgumentException("'polygon' cannot be null")
//     // Result
//     val (vertices, indices) = vertsAndIndicesFromPolygon(polygon)
//     new RenderData(
//       _vertices = vertices,
//       _indices = indices,
//       _layer = layer,
//       _color = color,
//       _transform = transform,
//       _shaderOverride = shaderOverride,
//       _extraUniforms = extraUniforms
//     )
//   }

//   def fromPolyline(
//       polyline: Polyline,
//       width: Float,
//       layer: Float = 0,
//       color: Color = Color.WHITE,
//       transform: Matrix3 = Matrix3.IDENTITY,
//       shaderOverride: Shader = null,
//       extraUniforms: Map[String, Uniform] = Map.empty
//   ): RenderData = {
//     // Exceptions
//     if polyline == null
//     then throw new IllegalArgumentException("'polyline' cannot be null")
//     // Result
//     val (vertices, indices) = vertsAndIndicesFromPolyline(polyline, width)
//     new RenderData(
//       _vertices = vertices,
//       _indices = indices,
//       _layer = layer,
//       _color = color,
//       _transform = transform,
//       _shaderOverride = shaderOverride,
//       _extraUniforms = extraUniforms
//     )
//   }

//   def fromNGon(
//       ngon: NGon,
//       layer: Float = 0,
//       color: Color = Color.WHITE,
//       transform: Matrix3 = Matrix3.IDENTITY,
//       shaderOverride: Shader = null,
//       extraUniforms: Map[String, Uniform] = Map.empty
//   ): RenderData = {
//     // Exceptions
//     if ngon == null
//     then throw new IllegalArgumentException("'ngon' cannot be null")
//     // Result
//     val (vertices, indices) = vertsAndIndicesFromNGon(ngon)
//     new RenderData(
//       _vertices = vertices,
//       _indices = indices,
//       _layer = layer,
//       _color = color,
//       _transform = transform,
//       _shaderOverride = shaderOverride,
//       _extraUniforms = extraUniforms
//     )
//   }
// }
