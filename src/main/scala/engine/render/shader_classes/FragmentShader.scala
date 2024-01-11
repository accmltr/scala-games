package engine.render.shader_classes

final case class FragmentShader(path: String) extends ShaderData {
  val source: String = engine.io.readTextFile(path)
}
