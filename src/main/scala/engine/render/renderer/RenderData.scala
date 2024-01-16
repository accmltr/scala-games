package engine.render.renderer

import engine.render.shader.{Uniform, Shader}
import engine.render.Color
import engine.math.Matrix3
import engine.math.Vector2
import engine.math.geometry.Line
import scala.util.boundary, boundary.break
import render_data_util.{indicesFromPolygon, verticesFromPolygon}

inline def defaultShader: Shader = Shader(
  "src/main/scala/engine/render/shaders/default/default.vert",
  "src/main/scala/engine/render/shaders/default/default.frag"
)

case class RenderData(
    val vertices: Array[Float],
    val indices: Array[Int],
    val uniforms: Map[String, Uniform] = Map.empty,
    val shader: Shader,
    val layer: Float = 0,
    val tint: Color = Color.WHITE,
    val transform: Matrix3 = Matrix3.IDENTITY
) {

  // Exceptions
  if shader == null
  then throw new IllegalArgumentException("'shader' cannot be null")
  if tint == null
  then throw new IllegalArgumentException("'tint' cannot be null")
  if transform == null
  then throw new IllegalArgumentException("'transform' cannot be null")
  if vertices.length < 3
  then throw new IllegalArgumentException("At least 3 vertices are required")
  if indices.length < 3
  then throw new IllegalArgumentException("At least 3 indices are required")
  if uniforms.contains("layer")
  then throw new IllegalArgumentException("Uniform 'layer' is reserved")
  if uniforms.contains("tint")
  then throw new IllegalArgumentException("Uniform 'tint' is reserved")
  if uniforms.contains("transform")
  then throw new IllegalArgumentException("Uniform 'transform' is reserved")

}

object RenderData {
  import engine.math.shapes.*

  def fromPolygon(
      polygon: Polygon,
      uniforms: Map[String, Uniform] = Map.empty,
      shader: Shader = defaultShader,
      layer: Float = 0,
      tint: Color = Color.WHITE,
      transform: Matrix3 = Matrix3.IDENTITY
  ): RenderData = {
    // Exceptions
    if polygon == null
    then throw new IllegalArgumentException("'points' cannot be null")
    // Result
    new RenderData(
      vertices = verticesFromPolygon(polygon),
      indices = indicesFromPolygon(polygon),
      uniforms = uniforms,
      shader = shader,
      layer = layer,
      tint = tint,
      transform = transform
    )
  }
}
