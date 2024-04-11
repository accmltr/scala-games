import engine.core.Entity
import engine.core.World
import engine.*
import engine.input.KeyCode
import engine.math.*
import engine.math.geometry.*
import engine.math.shapes.*
import engine.render.*
import engine.render.renderer.*
import engine.render.renderer.render_element.*
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
import javax.swing.InputMap
import engine.render.window.AA
import general.Wolf

object MyGame extends World {

  title = "MyGame"

  // onEntityReady += (e => println(s"Entity Created: " + e.name))
  // onEntityDestroyQueued += (e => println(s"Entity Destroy Queued: " + e.name))
  // onEntityDestroyed += (e => println(s"Entity Destroyed: " + e.name))

  val diamond = Polygon(
    Vector(
      Vector2(-1, 0),
      Vector2(0, 2),
      Vector2(1, 0),
      Vector2(0, -1.3)
    ).map(_ * 13)
  )
  val wolf = Wolf("Razor")
  wolf.addRenderElement(PolygonRenderElement(diamond * 3))
  wolf.globalPosition = Vector2(500, 400)
  val wolfCub = Wolf("Razorine")
  wolf.addChild(wolfCub)
  wolfCub.localPosition = Vector2(70, -30)
  wolfCub.addRenderElement(
    PolygonRenderElement(diamond)
  )
  println(s"Wolf children: ${wolf.children}")
  println(s"WolfCub parent: ${wolfCub.parent}")

  window.resolution = Resolution.p720
  window.maximized = false
  window.fpsStats.showAvg = true
  window.backgroundColor = Vector3.one * 0.12
  window.anti_aliasing = AA.x8

  window.vsync = true
  onInit += { (_) =>
    window.setCursor("res/cursor.png", 0, 0)

    // println(s"Wolf rotation 1: ${wolf.globalRotation}")
    // wolf.globalRotation = pi / 4
    // println(s"Wolf rotation 2: ${wolf.globalRotation}")
    // wolf.globalRotation = pi / 2
    // println(s"Wolf rotation 3: ${wolf.globalRotation}")
    // wolf.globalRotation = pi
    // println(s"Wolf rotation 4: ${wolf.globalRotation}")
    // wolf.globalRotation = 3 * pi / 2
    // println(s"Wolf rotation 5: ${wolf.globalRotation}")
    // wolf.globalRotation = 2 * pi
    // println(s"Wolf rotation 6: ${wolf.globalRotation}")
    // wolf.globalRotation = -pi / 2
    // println(s"Wolf rotation 7: ${wolf.globalRotation}")
    // wolf.globalRotation = -pi
    // println(s"Wolf rotation 8: ${wolf.globalRotation}")
    // wolf.globalRotation = -3 * pi / 2
    // println(s"Wolf rotation 9: ${wolf.globalRotation}")
    // wolf.globalRotation = -2 * pi
    // println(s"Wolf rotation 10: ${wolf.globalRotation}")
    // wolf.globalRotation = 0
    // println(s"Wolf rotation 11: ${wolf.globalRotation}")
  }

  onUpdate += { (delta: Float) =>

    wolf.globalPosition = input.mousePosition
    // wolf.globalRotation =
    //   (-pi / 2f) + (input.mousePosition - wolf.globalPosition).angle

    // Rotate Wolf
    if (input.pressed(KeyCode.w))
      wolf.localRotation -= pi * delta * 0.4f
    if (input.pressed(KeyCode.e))
      wolf.globalRotation += pi * delta * 0.4f
    // Scale Wolf
    if (input.pressed(KeyCode.s))
      wolf.globalScale -= Vector2.one * delta
    if (input.pressed(KeyCode.d))
      wolf.globalScale += Vector2.one * delta
    // Rotate Wolf Cub
    if (input.pressed(KeyCode.r))
      wolfCub.localRotation -= pi * delta * 0.4f
    if (input.pressed(KeyCode.t))
      wolfCub.localRotation += pi * delta * 0.4f
    // Scale Wolf Cub
    if (input.pressed(KeyCode.f))
      wolfCub.localScale -= Vector2.one * delta
    if (input.pressed(KeyCode.g))
      wolfCub.localScale += Vector2.one * delta

    if (input.justPressed(KeyCode.v))
      window.vsync = !window.vsync

    if (input.justReleased(KeyCode.escape))
      quit()
  }

  run()
}
