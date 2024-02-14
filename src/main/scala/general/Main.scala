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
  val anti_aliasing_shader = Shader(
    "src/main/scala/engine/render/shaders/vertex/default.vert",
    "src/main/scala/engine/render/shaders/post_processing/anti_aliasing.frag"
  )

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

    if (glCheckFramebufferStatus(fbo) == GL_FRAMEBUFFER_COMPLETE)
      println(s"Successfully created a Frame Buffer Object.")

    // Make the default FBO active again
    glBindFramebuffer(GL_FRAMEBUFFER, 0)

    // < Create FBO -----------------------------------------

    // renderer.render(
    //   List(
    //     // sprite,
    //     circleSdf
    //   ).map(_.renderData)
    // )
    if (!sprite.loaded)
      sprite.load()
    renderer.renderSprites(List(sprite))

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
