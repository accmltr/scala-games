package engine.render.shader_classes

final case class VertexShader(path: String) extends ShaderData {
  val source: String = engine.io.readTextFile(path)
}
