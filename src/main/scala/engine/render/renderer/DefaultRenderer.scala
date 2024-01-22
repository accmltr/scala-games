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
import render_data.RenderData
import engine.Time

final case class DefaultRenderer(
    override val window: Window
) extends Renderer(window) {
  override def render(
      renderDatas: List[RenderData],
      wireframeMode: Boolean = false
  ): Unit = {
    for rd <- renderDatas.sortBy(_.layer)
    do {

      // Create and bind a VAO
      val vaoId = glGenVertexArrays()
      glBindVertexArray(vaoId)

      // Create and bind a VBO for the vertices
      val vboId = glGenBuffers()
      glBindBuffer(GL_ARRAY_BUFFER, vboId)
      glBufferData(GL_ARRAY_BUFFER, rd.vertices, GL_STATIC_DRAW)
      glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
      glEnableVertexAttribArray(0)

      // Create and bind a VBO for the indices
      val eboId = glGenBuffers()
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, rd.indices, GL_STATIC_DRAW)

      // Unbind the VAO
      glBindVertexArray(0)

      // Bind shader program
      rd.shader.use()

      // Upload uniforms
      rd.shader.uploadUniforms(
        rd.extraUniforms ++ Map[String, Uniform](
          BuiltInUniforms.uRes.toString() -> window.resolution.toVector2,
          BuiltInUniforms.uLayer.toString() -> rd.layer,
          BuiltInUniforms.uColor.toString() -> Vector4(
            rd.color.r,
            rd.color.g,
            rd.color.b,
            rd.color.a
          ),
          BuiltInUniforms.uTrans.toString() -> rd.transform,
          BuiltInUniforms.uTime.toString() -> Time.current
        )
      )

      // Set wireframe mode
      if wireframeMode
      then glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)

      // Draw
      glBindVertexArray(vaoId)
      glDrawElements(
        GL_TRIANGLES,
        rd.indices.length,
        GL_UNSIGNED_INT,
        0
      )
      glBindVertexArray(0)

    }

  }
}
