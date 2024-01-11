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
import engine.render.shader_classes.Shader
import org.joml.Matrix4f
import java.nio.IntBuffer
import java.nio.FloatBuffer
import engine.math.shapes.Circle
import engine.render.shape_renderer.CircleRenderer
import engine.math.sin
import engine.Time
import engine.math.cos
import engine.render.RenderMaster
import engine.render.render_manager.MeshRenderManager
import engine.render.rendered_element.RenderedMesh
import engine.render.mesh.Mesh
import engine.render.Color
import engine.math.Matrix3
import engine.math.Matrix4
import engine.math.Matrix3.transform
import engine.math.pi
import engine.math.shapes.Polygon

object MyGame extends Game {

  title = "MyGame"

  val shader = Shader(
    "src/main/scala/general/shaders/experiment.vert",
    "src/main/scala/general/shaders/experiment.frag"
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

  val renderMaster: RenderMaster = RenderMaster()
  val meshRenderManager: MeshRenderManager = MeshRenderManager()

  onInit += { (_) =>

    window.vsync = true
    shader.compile()
  }

  onUpdate += { (delta: Float) =>
    shader.uploadFloat("aspect", window.aspect)
    shader.uploadVec2f("resolution", window.resolution.toVector2)

    val r = RenderedMesh(
      shader = shader,
      transform = Matrix3
        .transform(
          translation = Vector2(
            cos(1.7f * Time.current) * .2,
            sin(2f * Time.current) * .11
          ),
          rotation = cos(.5f * Time.current) * 2f * pi,
          scale = Vector2(1f, 1f) * .3f
        ),
      mesh = Mesh(Circle(0.5f), 7),
      tint = Color.GRAY
    )
    val r1 = RenderedMesh(
      shader = shader,
      transform = Matrix3
        .transform(
          translation = Vector2(
            cos(1.7f * Time.current + 3f) * .2,
            sin(2f * Time.current + 3f) * .11
          ),
          rotation = cos(.5f * Time.current + 3f) * 2f * pi,
          scale = Vector2(1f, 1f) * .3f
        ),
      mesh = Mesh(Circle(0.5f), 7),
      tint = Color.GREEN * .75
    )
    val p1 = RenderedMesh(
      shader = shader,
      transform = Matrix3
        .transform(
          translation = Vector2(
            cos(1.7f * Time.current + 5f) * .2,
            sin(3f * Time.current + 3f) * .11
          ),
          rotation = cos(.8f * Time.current + 3f) * 2f * pi,
          scale = Vector2(1f, 1f) * .3f
        ),
      mesh = Mesh(
        Polygon(
          Vector(
            Vector2(0, 0),
            Vector2(1, 0),
            Vector2(1, 1),
            Vector2(0, 1),
            Vector2(0.7, 0.5)
          )
        )
      ),
      tint = Color.YELLOW * .75
    )
    val l = engine.render.rendered_element.Line(
      points = Array(
        Vector2(0, 0),
        Vector2(-1, -1),
        Vector2(100, -100)
        // Vector2(0, -1),
        // Vector2(-1, -1),
        // Vector2(-3, 1),
        // Vector2(0, 10)
      ).map(_ * .8),
      width = 0.07,
      shader = shader
    )

    renderMaster += r
    renderMaster += r1
    renderMaster += p1
    renderMaster += l

    // glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
    renderMaster.render()
    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

    renderMaster -= r
    renderMaster -= r1
    renderMaster -= p1
    renderMaster -= l

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
