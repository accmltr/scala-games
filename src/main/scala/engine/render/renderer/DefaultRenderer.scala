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
import engine.Time
import java.nio.ByteBuffer
import org.lwjgl.BufferUtils
import engine.render.Image
import org.lwjgl.glfw.GLFWImage
import engine.render.renderer.render_element.Sprite

final case class DefaultRenderer(
    override val window: Window
) extends Renderer(window) {

  var fboId: Int = 0
  var textureId: Int = 0

  override def render(
      renderDatas: List[RenderData],
      wireframeMode: Boolean = false
  ): Unit = {

    // Frame Buffer Object
    {
      // Prepare for post-processing
      glBindFramebuffer(GL_FRAMEBUFFER, 0)
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      glDisable(GL_DEPTH_TEST)

      // Create FBO
      fboId = glGenFramebuffers()
      glBindFramebuffer(GL_FRAMEBUFFER, fboId)

      // Create texture
      textureId = glGenTextures()
      glBindTexture(GL_TEXTURE_2D, textureId)
      val pixels = BufferUtils.createByteBuffer(
        window.resolution.width * window.resolution.height * 3
      )
      glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGB,
        window.resolution.width,
        window.resolution.height,
        0,
        GL_RGB,
        GL_UNSIGNED_BYTE,
        pixels
      )
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
      glFramebufferTexture2D(
        GL_FRAMEBUFFER,
        GL_COLOR_ATTACHMENT0,
        GL_TEXTURE_2D,
        textureId,
        0
      )

      // Check for errors
      if glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE
      then throw new RuntimeException("Failed to create FBO")
    }

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
      else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

      // Draw
      glBindVertexArray(vaoId)
      glDrawElements(
        GL_TRIANGLES,
        rd.indices.length,
        GL_UNSIGNED_INT,
        0
      )
      glBindVertexArray(0)

      // Clean up
      glDeleteVertexArrays(vaoId)
      glDeleteBuffers(vboId)
      glDeleteBuffers(eboId)
    }

    // Reset wireframe mode
    if wireframeMode
    then glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

    // Reset shader program
    glUseProgram(0)
  }

  override def renderSprites(sprites: List[Sprite]): Unit = {
    sprites.foreach(_.render(window))
  }

  override def applyPostProcessing(shaders: List[Shader]): Unit = {

    // Vertices for a fullscreen quad
    val vertices: Array[Float] = Array(
      -1, -1, 1, -1, 1, 1, 1, 1, -1, 1, -1, -1
    )

    // Elements for a fullscreen quad
    val elements: Array[Int] = Array(0, 1, 2, 2, 3, 0)

    // Create and bind a VAO
    val vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    // Create and bind a VBO for the vertices
    val vboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(0)

    // Create and bind a VBO for the indices
    val eboId = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW)

    // Unbind the VAO
    glBindVertexArray(0)

    // Apply each shader
    for shader <- shaders
    do {

      // Bind shader program
      shader.use()

      shader.uploadUniforms(
        Map[String, Uniform](
          BuiltInUniforms.uRes.toString() -> window.resolution.toVector2,
          BuiltInUniforms.uTime.toString() -> Time.current
          // "uScreenTexture" -> TexturePointer(0, textureId)
        )
      )

      // Draw
      glBindVertexArray(vaoId)
      glDrawElements(
        GL_TRIANGLES,
        elements.length,
        GL_UNSIGNED_INT,
        0
      )
      glBindVertexArray(0)
    }

    // Clean up
    glDeleteFramebuffers(fboId)
    glDeleteTextures(textureId)
    glDeleteVertexArrays(vaoId)
    glDeleteBuffers(vboId)
    glDeleteBuffers(eboId)
  }
}
