import engine.*
import engine.input.KeyCode
import engine.math.*
import engine.math.geometry.*
import engine.math.shapes.*
import engine.render.*
import engine.render.renderer.*
import engine.render.renderer.render_element.*
import engine.render.renderer.sdf.*
import engine.render.shader.Shader
import engine.render.window.Resolution
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*

import java.nio.CharBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import scala.io.Source
import engine.render.Image
import java.nio.ByteBuffer

object MyGame extends Game {

  title = "MyGame"

  val renderer = DefaultRenderer(window)

  root = {
    val node = Node()
    node.name = "MyFirstNode"
    node.position = Vector2(0, 0)
    node.scale = Vector2(1, 1)
    node.rotation = 0
    node.children = List(
      Node(),
      Node(),
      Node(),
      Node()
    )
    node
  }

  println(root)

  window.resolution = Resolution.p720
  window.maximized = false
  window.fpsStats.showAvg = true
  window.backgroundColor = Vector3(0.1f, 0.1f, 0.1f)

  val circleSdf = CircleSdf(6.0f)
  circleSdf.borderColor = Color.GREEN
  circleSdf.color = Color.BLUE

  var sprite: Sprite = Sprite(
    "res/sample_image.png"
  )

  sprite.position = Vector2(700, 500)
  sprite.width = 300
  sprite.height = 300
  sprite.rotation = pi / 3.0f

  window.vsync = true
  onInit += { (_) =>
    window.setCursor("res/cursor.png", 0, 0)
  }

  onUpdate += { (delta: Float) =>

    circleSdf.bow = circleSdf.radius * abs(sin(Time.current))
    circleSdf.position = input.mousePosition

    import org.lwjgl.opengl.GL30._

    // Create FBO -----------------------------------------

    val fboPointers = Array(1)
    glGenFramebuffers(fboPointers)
    val fbo = fboPointers(0)
    glBindFramebuffer(GL_FRAMEBUFFER, fbo)

    // Create texture for FBO
    val texturePointers = Array(1)
    glGenTextures(texturePointers)
    val texture = texturePointers(0)
    glBindTexture(GL_TEXTURE_2D, texture)
    glTexImage2D(
      GL_TEXTURE_2D,
      0,
      GL_RGB,
      window.resolution.width,
      window.resolution.height,
      0,
      GL_RGB,
      GL_UNSIGNED_BYTE,
      null.asInstanceOf[ByteBuffer]
    )
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glBindTexture(GL_TEXTURE_2D, 0)
    glFramebufferTexture2D(
      GL_FRAMEBUFFER,
      GL_COLOR_ATTACHMENT0,
      GL_TEXTURE_2D,
      texture,
      0
    )

    val rboPointers = Array(1)
    glGenRenderbuffers(rboPointers)
    val rbo = rboPointers(0)
    glBindRenderbuffer(GL_RENDERBUFFER, rbo)
    glRenderbufferStorage(
      GL_RENDERBUFFER,
      GL_DEPTH24_STENCIL8,
      window.resolution.width,
      window.resolution.height
    )
    glBindRenderbuffer(GL_RENDERBUFFER, 0)
    glFramebufferRenderbuffer(
      GL_FRAMEBUFFER,
      GL_DEPTH_STENCIL_ATTACHMENT,
      GL_RENDERBUFFER,
      rbo
    )

    if (glCheckFramebufferStatus(fbo) == GL_FRAMEBUFFER_COMPLETE)
      println(s"Successfully created a Frame Buffer Object.")
    glBindFramebuffer(GL_FRAMEBUFFER, 0)
    // < Create FBO -----------------------------------------

    // Use custom FBO
    glBindFramebuffer(GL_FRAMEBUFFER, fbo)
    // renderer.render(
    //   List(
    //     // sprite,
    //     circleSdf
    //   ).map(_.renderData)
    // )
    if (!sprite.loaded)
      sprite.load()
    renderer.renderSprites(List(sprite))
    // Bind default FBO and clean it
    glBindFramebuffer(GL_FRAMEBUFFER, 0)
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    glClear(GL_COLOR_BUFFER_BIT)

    // Render shader and pass custom FBO texture2D as a uniform
    glBindTexture(GL_TEXTURE_2D, texture)
    val anti_aliasing_shader = Shader(
      "src/main/scala/engine/render/shaders/post_processing/anti_aliasing.vert",
      "src/main/scala/engine/render/shaders/post_processing/anti_aliasing.frag"
    )
    anti_aliasing_shader.use()

    // Create and bind a VAO
    val vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    // Create and bind a VBO for the vertices
    val swidth = window.resolution.width
    val sheight = window.resolution.height
    val vertices: Array[Float] =
      Array(0, 0, 1, 0, 1, 1, 0, 1)
    val indices: Array[Int] = Array(0, 1, 2, 2, 3, 0)
    val vboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(0)

    // Create and bind a VBO for the indices
    val eboId = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

    // Upload uniforms
    val varLocation: Int =
      glGetUniformLocation(anti_aliasing_shader.id, "uScreenTexture")
    glUniform1i(varLocation, GL_TEXTURE0)

    // Draw
    glDrawElements(
      GL_TRIANGLES,
      indices.length,
      GL_UNSIGNED_INT,
      0
    )
    glBindVertexArray(0)

    // Clean up
    glDeleteVertexArrays(vaoId)
    glDeleteBuffers(vboId)
    glDeleteBuffers(eboId)

    // Unbind texture
    glBindTexture(GL_TEXTURE_2D, 0)

    // Reset shader program
    import org.lwjgl.opengl.GL20._
    glUseProgram(0)

    // renderer.applyPostProcessing(List(anti_aliasing_shader))

    // Delete FBO -----------------------------------------
    glDeleteBuffers(fbo)
    // < Delete FBO -----------------------------------------

    if (input.justPressed(KeyCode.v)) {
      window.vsync = !window.vsync
    }

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }
  }

  run()
}
