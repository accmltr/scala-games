import engine.input.{KeyCode, MouseCode}
import engine.Game
import engine.input.KeyCode
import engine.math.Vector2
import engine.math.Vector2Implicits.given
import engine.input.MouseCode
import engine.rendering.window.{ScreenSize, FpsStats}
import engine.Node
import engine.Component
import engine.rendering.shaders.Shader
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.BufferUtils
import engine.math.Vector3
import engine.rendering.shaders.Shader
import org.joml.Matrix4f
import java.nio.IntBuffer
import java.nio.FloatBuffer
import engine.math.geometry.Circle

object MyGame extends Game {

  // TEMP: For Practice
  var quadRenderer: QuadRenderer = _
  var polygonRenderer: PolygonRenderer = _

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

  window.size = ScreenSize.p720
  window.maximized = false
  window.vsync = true
  window.fpsStats.showAvg = true

  onInit += { (_) =>
    quadRenderer = QuadRenderer()
    polygonRenderer = PolygonRenderer(
      Circle(.7, 100).vertices
    )
    shader.compile()
  }
  println("Added onInit callback")

  onUpdate += { (delta: Float) =>
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    // quadRenderer.render(shader)
    polygonRenderer.render(shader)
    // shader.use()
    // extra.update(delta)
  }

  run()
}
class PolygonRenderer(
    vertices: Vector[engine.math.Vector2]
) {

  // vertices to FloatBuffer
  var fbVerts = BufferUtils.createFloatBuffer(vertices.length * 2)
  vertices.foreach { v =>
    fbVerts.put(v.x)
    fbVerts.put(v.y)
  }
  fbVerts.flip()

  val vaoId = glGenVertexArrays()
  glBindVertexArray(vaoId)

  val vboId = glGenBuffers()
  glBindBuffer(GL_ARRAY_BUFFER, vboId)
  glBufferData(GL_ARRAY_BUFFER, fbVerts, GL_STATIC_DRAW)
  glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
  glEnableVertexAttribArray(0)

  def render(shader: Shader): Unit = {
    shader.use()
    glBindVertexArray(vaoId)
    glDrawArrays(GL_POLYGON, 0, vertices.length)
    glBindVertexArray(0)
  }
}

class QuadRenderer {
  val vertices: FloatBuffer = BufferUtils
    .createFloatBuffer(8)
    .put(
      Array(
        -0.5f, -0.5f, // Bottom left corner
        0.5f, -0.5f, // Bottom right corner
        0.5f, 0.5f, // Top right corner
        -0.5f, 0.5f // Top left corner
      )
    )
    .flip()

  val indices: IntBuffer = BufferUtils
    .createIntBuffer(6)
    .put(
      Array(
        0, 1, 2, // First triangle
        2, 3, 0 // Second triangle
      )
    )
    .flip()

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
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)
    glBindVertexArray(0)
  }
}
