import java.nio.{FloatBuffer, IntBuffer}

import engine.*
import engine.input.KeyCode
import engine.math.*
import engine.math.geometry.*
import engine.math.shapes.*
import engine.render.*
import engine.render.renderer.*
import engine.render.renderer.render_element.*
import engine.render.shader.Shader
import engine.render.window.Resolution
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import engine.render.renderer.sdf.*

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

  val rectSdf = RectSdf(200.0f, 200.0f)
  rectSdf.position = Vector2(1, 1)
  rectSdf.cr = 10
  rectSdf.borderColor = Color.YELLOW
  rectSdf.layer = 100
  rectSdf.color = Color.ORANGE
  rectSdf.bow = 10
  rectSdf.biw = 10

  val circleSdf = CircleSdf(100.0f)
  circleSdf.position = Vector2(.24, .24)
  circleSdf.borderColor = Color.GREEN
  circleSdf.color = Color.BLUE

  onInit += { (_) =>
    window.vsync = true
  }

  onUpdate += { (delta: Float) =>
    val k = 0.7f
    // rectSdf.biw = 10 * abs(sin(Time.current * k))

    circleSdf.bow = circleSdf.radius * abs(sin(Time.current * k))

    renderer.render(
      List(
        rectSdf,
        circleSdf
      ).map(_.renderData)
    )

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }
  }

  run()
}
