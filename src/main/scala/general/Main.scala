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
import engine.render.renderer.sdf.CircleSdf

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

  import engine.render.renderer.render_element.PolygonRenderElement
  val poly: PolygonRenderElement = PolygonRenderElement(
    points = Vector(
      Vector2(0, 0),
      Vector2(1, 0),
      Vector2(1, 1),
      Vector2(0, 1),
      Vector2(0, .8),
      Vector2(0.35, 0.5),
      Vector2(0, .2)
    ).map(v => Vector2(v.x - .5, v.y - .5))
  )
  poly.layer = 0
  poly.color = Color.WHITE
  val iris = CircleSdf(
    radius = 0.308f,
    borderColor = Color.BLACK,
    color = Color.WHITE,
    layer = 1
  )
  val pupil = NGonRenderElement(
    radius = 0.13f
  )
  pupil.color = Color.BLACK
  pupil.layer = 1.1
  val polyline = PolylineRenderElement(
    points = List(
      Vector2(0, 0),
      Vector2(1, 0),
      Vector2(1, 1),
      Vector2(0, 1),
      Vector2(0, .8),
      Vector2(0.35, 0.5),
      Vector2(0, .2),
      Vector2(0, 0)
    ).map(v => Vector2(v.x - .5, v.y - .5)),
    width = 0.01f
  )
  // var polygonRenderData: RenderData = _
  // var polygonRenderData_no2: RenderData = _
  // var polygonRenderData_no3: RenderData = _
  // var polygonRenderData_no4: RenderData = _
  // var polylineRenderData: RenderData = _
  // var ngonRenderData: RenderData = _
  // var ngonRenderData_no2: RenderData = _

  onInit += { (_) =>
    window.vsync = true

    // val ngon = NGon(0.5f, 7)
    // ngonRenderData = RenderData.fromNGon(ngon, color = Color.YELLOW)
    // ngonRenderData_no2 = RenderData.fromNGon(
    //   NGon(0.2f, 300),
    //   color = Color.RED,
    //   transform = Matrix3.transform(
    //     translation = Vector2(-0.2, -0.3),
    //     rotation = 0
    //   )
    // )

    // val polyline = Polyline(
    //   List(
    //     Vector2(0, 0),
    //     Vector2(1, 0),
    //     Vector2(1, 1),
    //     Vector2(0, 1),
    //     Vector2(0, .8),
    //     Vector2(0.35, 0.5),
    //     Vector2(0, .2),
    //     Vector2(0, 0)
    //   ).map(v => Vector2(v.x - .5, v.y - .5))
    // )
    // polylineRenderData = RenderData.fromPolyline(polyline, 0.01f)

    // val polygon = Polygon(
    //   Vector(
    //     Vector2(0, 0),
    //     Vector2(1, 0),
    //     Vector2(1, 1),
    //     Vector2(0, 1),
    //     Vector2(0, .8),
    //     Vector2(0.35, 0.5),
    //     Vector2(0, .2)
    //   ).map(v => Vector2(v.x - .5, v.y - .5))
    // )
    // polygonRenderData = RenderData.fromPolygon(
    //   polygon = polygon,
    //   color = Color.YELLOW * .75
    // )
    // polygonRenderData_no2 = RenderData.fromPolygon(
    //   polygon = Polygon(
    //     Vector(
    //       Vector2(0, 0),
    //       Vector2(1, 0),
    //       Vector2(1, 1),
    //       Vector2(0.5, 1.3),
    //       Vector2(0, 1),
    //       Vector2(0, .8),
    //       Vector2(0.35, 0.2)
    //     ).map(v => Vector2(v.x - .5, v.y - .5))
    //   ),
    //   color = Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, 0.1),
    //   transform = Matrix3.transform(
    //     translation = Vector2(0.5, 0.5),
    //     rotation = 0,
    //     scale = Vector2(1, 1) * 0.5
    //   ),
    //   extraUniforms = Map(
    //     "uTimeMult" -> .2f
    //   ),
    //   layer = -10
    // )
    // polygonRenderData_no3 = RenderData.fromPolygon(
    //   polygon = Polygon(polygon.points.take(4)),
    //   color = Color.BLUE * .75,
    //   transform = Matrix3.transform(
    //     translation = Vector2(-0.5, 0.5),
    //     rotation = 0,
    //     scale = Vector2(1, 1) * 0.5
    //   )
    // )
    // polygonRenderData_no4 = RenderData.fromPolygon(
    //   polygon = Polygon(polygon.points.take(3)),
    //   color = Color.RED * .75,
    //   transform = Matrix3.transform(
    //     translation = Vector2(0, -0.5),
    //     rotation = 0,
    //     scale = Vector2(1, 1) * 0.5
    //   )
    // )
  }

  onUpdate += { (delta: Float) =>

    poly.color = Color(
      sin(1.7f * Time.current) * .5f + .5f,
      sin(2f * Time.current) * .5f + .5f,
      sin(3f * Time.current) * .5f + .5f
    )
    pupil.position = Vector2(
      (input.mousePosition.x - window.resolution.width / 2) / window.resolution.width,
      -(input.mousePosition.y - window.resolution.height / 2) / window.resolution.height
    ) * 0.27f

    iris.borderInnerWidth = 0.007f + abs(0.008f * cos(2f * Time.current))

    renderer.render(
      List(
        poly,
        iris,
        pupil,
        polyline
      ).map(_.renderData)
    )

    // polygonRenderData = polygonRenderData.copy(
    //   transform = Matrix3
    //     .transform(
    //       translation = Vector2(
    //         2f * (input.mousePosition.x / window.resolution.width) - 1,
    //         2f * (-input.mousePosition.y / window.resolution.height) + 1
    //       ),
    //       rotation = cos(.8f * Time.current + 3f) * 2f * pi,
    //       scale = Vector2(1f, 1f) * (.2f + .1f * (cos(.8f * Time.current) + 1))
    //     )
    // )

    // renderer.render(
    //   List(
    //     ngonRenderData,
    //     ngonRenderData_no2,
    //     polylineRenderData,
    //     polygonRenderData,
    //     polygonRenderData_no2,
    //     polygonRenderData_no3,
    //     polygonRenderData_no4
    //   ),
    //   wireframeMode = false // true
    // )

    // val r = RenderedMesh(
    //   shader = shader,
    //   transform = Matrix3
    //     .transform(
    //       translation = Vector2(
    //         cos(1.7f * Time.current) * .2,
    //         sin(2f * Time.current) * .11
    //       ),
    //       rotation = cos(.5f * Time.current) * 2f * pi,
    //       scale = Vector2(1f, 1f) * .3f
    //     ),
    //   mesh = Mesh(Circle(0.5f), 7),
    //   tint = Color.GRAY
    // )
    // val r1 = RenderedMesh(
    //   shader = shader,
    //   transform = Matrix3
    //     .transform(
    //       translation = Vector2(
    //         cos(1.7f * Time.current + 3f) * .2,
    //         sin(2f * Time.current + 3f) * .11
    //       ),
    //       rotation = cos(.5f * Time.current + 3f) * 2f * pi,
    //       scale = Vector2(1f, 1f) * .3f
    //     ),
    //   mesh = Mesh(Circle(0.5f), 7),
    //   tint = Color.GREEN * .75
    // )
    // val p1 = RenderedMesh(
    //   shader = shader,
    //   transform = Matrix3
    //     .transform(
    //       translation = Vector2(
    //         cos(1.7f * Time.current + 5f) * .2,
    //         sin(3f * Time.current + 3f) * .11
    //       ),
    //       rotation = cos(.8f * Time.current + 3f) * 2f * pi,
    //       scale = Vector2(1f, 1f) * (.2f + .1f * (cos(.8f * Time.current) + 1))
    //     ),
    //   mesh = Mesh(
    //     Polygon(
    //       Vector(
    //         Vector2(0, 0),
    //         Vector2(1, 0),
    //         Vector2(1, 1),
    //         Vector2(0, 1),
    //         Vector2(0, .8),
    //         Vector2(0.35, 0.5),
    //         Vector2(0, .2)
    //       ).map(v => Vector2(v.x - .5, v.y - .5))
    //     )
    //   ),
    //   tint = Color.YELLOW * .75
    // )
    // val l = LineRenderedElement(
    //   shader = lineShader,
    //   points = Array(
    //     Vector2(0, 0),
    //     Vector2(-1, -1) * .5,
    //     Vector2(1, -1) * .5,
    //     Vector2(1, 1) * .5
    //     // Vector2(0, -1),
    //     // Vector2(-1, -1),
    //     // Vector2(-3, 1),
    //     // Vector2(0, 10)
    //   ).map(_ * .8),
    //   width = 0.07
    // )

    // renderMaster += r
    // renderMaster += r1
    // renderMaster += p1
    // renderMaster += l

    // renderMaster.render()

    // renderMaster -= r
    // renderMaster -= r1
    // renderMaster -= p1
    // renderMaster -= l

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }

    // shader.uploadFloat("aspect", window.aspect)
    // shader.uploadVec2f("resolution", window.resolution.toVector2)
    // shader.uploadVec2f(
    //   "position",
    //   Vector2(sin(Time.current), .5 * cos(Time.current))
    // )
  }

  run()
}
