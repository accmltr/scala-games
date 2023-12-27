import engine.input.{KeyCode, MouseCode}
import engine.Game
import engine.input.KeyCode
import engine.math.Vector2
import engine.math.Vector2Implicits.given
import engine.input.MouseCode
import engine.render.window.{Resolution, FpsStats}
import engine.Node
import engine.Component
import engine.render.shader.Shader
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.BufferUtils
import engine.math.Vector3
import engine.render.shader.Shader
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

object MyGame extends Game {

  // TEMP: For Practice
  // var polygonRenderer: PolygonRenderer = _

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
  renderMaster += meshRenderManager

  onInit += { (_) =>
    // val circleRenderer: CircleRenderer = CircleRenderer(Circle(0.5f), 24)
    // polygonRenderer = PolygonRenderer(
    //   circleRenderer.vertices,
    //   circleRenderer.indices
    // )
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
    renderMaster += r

    renderMaster.render()

    renderMaster -= r

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }

    // shader.uploadFloat("aspect", window.aspect)
    // shader.uploadVec2f("resolution", window.resolution.toVector2)
    // shader.uploadVec2f(
    //   "position",
    //   Vector2(sin(Time.current), .5 * cos(Time.current))
    // )
    // polygonRenderer.render(shader)
  }

  run()
}

class PolygonRenderer(
    vertices: FloatBuffer,
    indices: IntBuffer
) {
// Create and bind a VAO
  val vaoId = glGenVertexArrays()
  glBindVertexArray(vaoId)

  // Create and bind a VBO for the vertices
  val vboId = glGenBuffers()
  glBindBuffer(GL_ARRAY_BUFFER, vboId)
  glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
  glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
  glEnableVertexAttribArray(0)

  // Create and bind a VBO for the indices
  val eboId = glGenBuffers()
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

  // Unbind the VAO
  glBindVertexArray(0)

  def render(shader: Shader): Unit = {
    shader.use()
    glBindVertexArray(vaoId)
    glDrawElements(GL_TRIANGLES, indices.limit(), GL_UNSIGNED_INT, 0)
    glBindVertexArray(0)
  }
}
