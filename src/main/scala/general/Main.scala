import engine.input.{KeyCode, MouseCode}
import engine.Game
import engine.input.KeyCode
import engine.math.Vector2
import engine.math.Vector2Implicits.given
import engine.input.MouseCode
import engine.render.window.{Resolution, FpsStats}
import engine.Node
import engine.Component
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.BufferUtils
import engine.math.Vector3
import org.joml.Matrix4f
import java.nio.IntBuffer
import java.nio.FloatBuffer
import engine.math.shapes.Circle
import engine.render.shape_renderer.CircleRenderer
import engine.math.sin
import engine.Time
import engine.math.cos
import engine.render.mesh.Mesh
import engine.render.Color
import engine.math.Matrix3
import engine.math.Matrix4
import engine.math.Matrix3.transform
import engine.math.pi
import engine.math.shapes.Polygon
import engine.render.renderer.*
import engine.render.shader.Shader
import engine.render.renderer.render_data.RenderData

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

  var polygonRenderData: RenderData = _
  var polygonRenderData_no2: RenderData = _
  var polygonRenderData_no3: RenderData = _
  var polygonRenderData_no4: RenderData = _

  onInit += { (_) =>
    window.vsync = true
    val polygon = Polygon(
      Vector(
        Vector2(0, 0),
        Vector2(1, 0),
        Vector2(1, 1),
        Vector2(0, 1),
        Vector2(0, .8),
        Vector2(0.35, 0.5),
        Vector2(0, .2)
      ).map(v => Vector2(v.x - .5, v.y - .5))
    )
    polygonRenderData = RenderData.fromPolygon(
      polygon = polygon,
      color = Color.YELLOW * .75
    )
    polygonRenderData_no2 = RenderData.fromPolygon(
      polygon = Polygon(
        Vector(
          Vector2(0, 0),
          Vector2(1, 0),
          Vector2(1, 1),
          Vector2(0.5, 1.3),
          Vector2(0, 1),
          Vector2(0, .8),
          Vector2(0.35, 0.2)
        ).map(v => Vector2(v.x - .5, v.y - .5))
      ),
      color = Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, 0.1),
      transform = Matrix3.transform(
        translation = Vector2(0.5, 0.5),
        rotation = 0,
        scale = Vector2(1, 1) * 0.5
      ),
      extraUniforms = Map(
        "uTimeMult" -> .2f
      ),
      layer = -10
    )
    polygonRenderData_no3 = RenderData.fromPolygon(
      polygon = Polygon(polygon.points.take(4)),
      color = Color.BLUE * .75,
      transform = Matrix3.transform(
        translation = Vector2(-0.5, 0.5),
        rotation = 0,
        scale = Vector2(1, 1) * 0.5
      )
    )
    polygonRenderData_no4 = RenderData.fromPolygon(
      polygon = Polygon(polygon.points.take(3)),
      color = Color.RED * .75,
      transform = Matrix3.transform(
        translation = Vector2(0, -0.5),
        rotation = 0,
        scale = Vector2(1, 1) * 0.5
      )
    )
  }

  onUpdate += { (delta: Float) =>

    polygonRenderData = polygonRenderData.copy(
      transform = Matrix3
        .transform(
          translation = Vector2(
            2f * (input.mousePosition.x / window.resolution.width) - 1,
            2f * (-input.mousePosition.y / window.resolution.height) + 1
          ),
          rotation = cos(.8f * Time.current + 3f) * 2f * pi,
          scale = Vector2(1f, 1f) * (.2f + .1f * (cos(.8f * Time.current) + 1))
        )
    )

    renderer.render(
      List(
        polygonRenderData,
        polygonRenderData_no2,
        polygonRenderData_no3,
        polygonRenderData_no4
      ),
      wireframeMode = false // true
    )

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
