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

object MyGame extends World {

  title = "MyGame"

  val renderer = DefaultRenderer(window)

  var myFirstEntity = Entity("My First Entity")
  for
    e <- myFirstEntity
    c <- Entity("Child 1")
  do e.addChild(c)

  // root = {
  //   val e = Entity()
  //   e.instance.name = "MyFirstNode"
  //   e.instance.position = Vector2(0, 0)
  //   e.instance.scale = Vector2(1, 1)
  //   e.instance.rotation = 0
  //   e.instance.children = List(
  //     Entity("Child 1"),
  //     Entity("Child 2"),
  //     Entity("Child 3"),
  //     Entity("Child 4")
  //   )
  //   e
  // }

  window.resolution = Resolution.p720
  window.maximized = false
  window.fpsStats.showAvg = true
  window.backgroundColor = Vector3.one * 0.12
  // window.anti_aliasing = AA.x4

  val ngonRenderElement = NGonRenderElement(100, 100)

  window.vsync = true
  onInit += { (_) =>
    window.setCursor("res/cursor.png", 0, 0)

    for e1 <- myFirstEntity
    do println(e1)
  }

  onUpdate += { (delta: Float) =>

    ngonRenderElement.position = input.mousePosition

    renderer.render(
      List(
        ngonRenderElement
      ).map(_.renderData)
    )

    if (input.justPressed(KeyCode.v)) {
      window.vsync = !window.vsync
    }

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }
  }

  run()
}
