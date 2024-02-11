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

  sprite.position = Vector2(50, 50)
  sprite.width = 100
  sprite.height = 100

  val cursor = Image("res/cursor.png")

  window.vsync = true
  onInit += { (_) =>
    window.setCursor(cursor, 0, 0)
  }

  onUpdate += { (delta: Float) =>

    circleSdf.bow = circleSdf.radius * abs(sin(Time.current))
    circleSdf.position = input.mousePosition

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

    if (input.justPressed(KeyCode.v)) {
      window.vsync = !window.vsync
    }

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }
  }

  run()
}
