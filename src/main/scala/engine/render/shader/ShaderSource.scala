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

  def vertexSource: Option[String] = engine.io.readTextFile(vertexPath)
  def fragmentSource: Option[String] = engine.io.readTextFile(fragmentPath)

  override def equals(x: Any): Boolean =
    x.match
      case ShaderSource(vertPath, fragPath) =>
        this.vertexPath == vertexPath && this.fragmentPath == fragmentPath
      case _ => false

}
