package engine.render.renderer

import engine.render.window.Window
import engine.render.shader.Uniform
import engine.render.shader.Shader
import engine.render.Color
import engine.math.Matrix3
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import engine.math.Vector3
import engine.math.Vector4

final case class DefaultRenderer(
    override val window: Window
) extends Renderer(window) {
  override def render(renderDatas: List[RenderData]): Unit = {
    for d <- renderDatas.sortBy(_.layer)
    do {

      // Create and bind a VAO
      val vaoId = glGenVertexArrays()
      glBindVertexArray(vaoId)

      // Create and bind a VBO for the vertices
      val vboId = glGenBuffers()
      glBindBuffer(GL_ARRAY_BUFFER, vboId)
      glBufferData(GL_ARRAY_BUFFER, d.vertices, GL_STATIC_DRAW)
      glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
      glEnableVertexAttribArray(0)

      // Create and bind a VBO for the indices
      val eboId = glGenBuffers()
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, d.indices, GL_STATIC_DRAW)

      // Unbind the VAO
      glBindVertexArray(0)

      // Bind shader program
      d.shader.use()

      // Upload uniforms
      d.shader.uploadUniforms(
        d.extraUniforms ++ Map[String, Uniform](
          "aspect" -> window.aspect,
          "resolution" -> window.resolution.toVector2,
          "layer" -> d.layer,
          "tint" -> Vector4(d.color.r, d.color.g, d.color.b, d.color.a),
          "transform" -> d.transform
        )
      )

      // Draw
      glBindVertexArray(vaoId)
      glDrawElements(
        GL_TRIANGLES,
        d.indices.length,
        GL_UNSIGNED_INT,
        0
      )
      glBindVertexArray(0)

    }

  }
}
