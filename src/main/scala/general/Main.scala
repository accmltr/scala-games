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

  val wolf = Wolf("Razor")
  val wolfCub = Wolf("Razorine")
  wolf.addChild(wolfCub)
  wolfCub.globalPosition = Vector2(150, 200)
  wolfCub.addRenderElement(
    NGonRenderElement(30, 30)
  )
  println(s"Wolf children: ${wolf.children}")
  println(s"WolfCub parent: ${wolfCub.parent}")

  window.resolution = Resolution.p720
  window.maximized = false
  window.fpsStats.showAvg = true
  window.backgroundColor = Vector3.one * 0.12
  // window.anti_aliasing = AA.x4

  val ngonRenderElement = NGonRenderElement(100, 100)
  wolf.addRenderElement(ngonRenderElement)

  window.vsync = true
  onInit += { (_) =>
    window.setCursor("res/cursor.png", 0, 0)
  }

  onUpdate += { (delta: Float) =>

    wolf.globalPosition = input.mousePosition

    if (input.justPressed(KeyCode.v)) {
      window.vsync = !window.vsync
    }

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }
  }

  run()
}
