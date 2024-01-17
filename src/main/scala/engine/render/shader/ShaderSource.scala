package engine.render.shader

final case class ShaderSource(
    val vertPath: String,
    val fragPath: String
) {
  override def equals(x: Any): Boolean =
    x.match
      case ShaderSource(vertPath, fragPath) =>
        this.vertPath == vertPath && this.fragPath == fragPath
      case _ => false

}
