package general

import engine.*
import engine.core.World
import engine.input.{KeyCode, MouseCode}
import engine.math.*
import engine.math.shapes.*
import engine.render.*
import engine.render.renderer.*
import engine.render.window.{AA, Resolution}
import org.lwjgl.opengl.GL11.*

object MyGame extends World {

  title = "MyGame"

  val diamond = Polygon(
    Vector(
      Vector2(-1, 0),
      Vector2(0, 2),
      Vector2(1, 0),
      Vector2(0, -1.3)
    ).map(_ * 13)
  )
  val wolf = Wolf("Razor")
//  wolf.addRenderElement(PolygonRenderElement(diamond * 3))
  wolf.globalPosition = Vector2(500, 400)
  val wolfCub = Wolf("Razorine")
  wolf.addChild(wolfCub)
  wolfCub.localPosition = Vector2(70, -30)
//  wolfCub.addRenderElement(
//    PolygonRenderElement(diamond)
//  )
  println(s"Wolf children: ${wolf.children}")
  println(s"WolfCub parent: ${wolfCub.parent}")

  val dyn4j = Dyn4j("My dyn4j instance")

  window.resolution = Resolution.p720
  window.maximized = false
  window.fpsStats.showAvg = true
  window.backgroundColor = Vector3.one * 0.12
  window.anti_aliasing = AA.x8
  window.vsync = true

  onInit += { _ =>
    window.setCursor("res/cursor.png", 0, 0)
  }
  var ipos = Vector2.zero
  var irot = 0f
  var iscale = Vector2.zero
  var mstart = Vector2.zero
  var _useMode = UseMode.None

  final private def useMode = _useMode

  final private def useMode_=(mode: UseMode): Unit =
    ipos = wolf.globalPosition
    irot = wolf.globalRotation
    iscale = wolf.globalScale
    mstart = input.mousePosition
    _useMode = mode

  enum UseMode {
    case None
    case Grab
    case Rotate
    case Scale
  }

  onUpdate += { (delta: Float) =>

    // wolf.globalPosition = input.mousePosition
    // wolf.globalRotation =
    //   (-pi / 2f) + (input.mousePosition - wolf.globalPosition).angle

    //    wolf.localScale = Vector2.one * sin(Time.current)

    useMode match
      case MyGame.UseMode.None => ()
      case UseMode.Grab =>
        wolf.globalPosition = input.mousePosition - mstart + ipos
      case UseMode.Rotate =>
        val ia = ipos.angleTo(mstart)
        val ca = ipos.angleTo(input.mousePosition)
        val da = ca - ia
        wolf.globalRotation = irot + da
      case UseMode.Scale =>
        val i = ipos.distanceTo(mstart)
        val c = ipos.distanceTo(input.mousePosition)
        val d = c / i
        wolf.globalScale = iscale * d

    // Rotate Wolf
    if (input.justPressed(KeyCode.q))
      useMode = UseMode.Grab
    if (input.justPressed(KeyCode.a))
      useMode = UseMode.Rotate
    if (input.justPressed(KeyCode.b))
      useMode = UseMode.Scale
    if (input.justPressed(MouseCode.left))
      useMode = UseMode.None
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
