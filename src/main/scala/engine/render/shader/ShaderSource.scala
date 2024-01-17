package engine.render.shader

/** Stateless read-only class that holds paths to vertex and fragment shader
  * source files.
  *
  * @param vertPath
  * @param fragPath
  */
final case class ShaderSource(
    val vertexPath: String,
    val fragmentPath: String
) {

  /** Validates whether the vertex and fragment shader source files exist.
    * @throws IllegalArgumentException
    *   If either the vertex or fragment shader source files do not exist.
    */
  def validate(): Unit = {
    if vertexSource.isEmpty
    then
      throw new IllegalArgumentException(
        s"Vertex shader not found: $vertexPath"
      )
    if fragmentSource.isEmpty
    then
      throw new IllegalArgumentException(
        s"Fragment shader not found: $fragmentPath"
      )
  }

  def vertexSource: Option[String] = engine.io.readTextFile(vertexPath)
  def fragmentSource: Option[String] = engine.io.readTextFile(fragmentPath)

  override def equals(x: Any): Boolean =
    x.match
      case ShaderSource(vertPath, fragPath) =>
        this.vertexPath == vertexPath && this.fragmentPath == fragmentPath
      case _ => false

}
