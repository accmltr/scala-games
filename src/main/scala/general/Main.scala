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
  // rectSdf.cr = 10
  rectSdf.borderColor = Color.YELLOW
  rectSdf.borderOuterWidth = 10
  rectSdf.borderInnerWidth = 10
  rectSdf.layer = 100
  rectSdf.color = Color(0.5f, 0.5f, 0.5f, 0.5f)

  val circleSdf = CircleSdf(100.0f)
  circleSdf.position = Vector2(.24, .24)
  circleSdf.borderColor = Color.GREEN
  circleSdf.color = Color.BLUE

  val cursor = Image("res/cursor.png")
  println(
    s"width: ${cursor.width}, height: ${cursor.height}, channels: ${cursor.channels}"
  )

  onInit += { (_) =>
    window.vsync = true

    window.setCursor(cursor, 0, 0)
  }

  onUpdate += { (delta: Float) =>
    val k = 0.7f
    // rectSdf.biw = 10 * abs(sin(Time.current * k))
    rectSdf.cr = 50 * abs(sin(Time.current * k))
    // rectSdf.biw = 50 * abs(sin(Time.current * k))

    circleSdf.bow = circleSdf.radius * abs(sin(Time.current * k))
    rectSdf.position =
      Vector2(0, -1) + input.mousePosition / window.resolution.height
    rectSdf.y *= -2

    renderer.render(
      List(
        rectSdf,
        circleSdf
      ).map(_.renderData)
    )

    if (input.justPressed(KeyCode.space)) {
      window.clearCursor()
    }

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }
  }

  run()
}
