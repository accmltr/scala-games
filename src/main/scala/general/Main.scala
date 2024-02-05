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
import engine.render.renderer.sdf.*
import java.nio.CharBuffer
import scala.io.Source

case class Image(path: String) {
  import org.lwjgl.glfw.GLFW._
  import org.lwjgl.glfw.GLFWImage;
  import org.lwjgl.system.MemoryStack;
  import org.lwjgl.stb.STBImage;
  import org.lwjgl.system.MemoryStack.stackPush;
  import org.lwjgl.system.MemoryUtil;
  import org.lwjgl.system.MemoryUtil.memAlloc;
  import org.lwjgl.system.MemoryUtil.memFree;
  import org.lwjgl.stb.STBImage.stbi_load_from_memory;
  import org.lwjgl.stb.STBImage.stbi_image_free;
  import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

  val image: GLFWImage = GLFWImage.malloc();
  val width: IntBuffer = BufferUtils.createIntBuffer(1);
  val height: IntBuffer = BufferUtils.createIntBuffer(1);
  val channels: IntBuffer = BufferUtils.createIntBuffer(1);

  private val imgData =
    org.lwjgl.stb.STBImage.stbi_load(path, width, height, channels, 4)

  image.set(width.get(0), height.get(0), imgData)

  def setAsCursor(windowId: Long, x: Int, y: Int): Unit = {
    val cursor = glfwCreateCursor(image, x, y)
    glfwSetCursor(windowId, cursor)
  }

  def free(): Unit = {
    stbi_image_free(imgData)
    image.free()
  }
}

object Image {
  def resetCursor(windowId: Long): Unit = {
    org.lwjgl.glfw.GLFW.glfwSetCursor(windowId, 0)
  }
}

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
  rectSdf.cr = 10
  rectSdf.borderColor = Color.YELLOW
  rectSdf.layer = 100
  rectSdf.color = Color.ORANGE
  rectSdf.bow = 10
  rectSdf.biw = 10

  val circleSdf = CircleSdf(100.0f)
  circleSdf.position = Vector2(.24, .24)
  circleSdf.borderColor = Color.GREEN
  circleSdf.color = Color.BLUE

  val cursor = Image("res/cursor.png")

  onInit += { (_) =>
    window.vsync = true

    cursor.setAsCursor(window.windowId, 0, 0)
  }

  onUpdate += { (delta: Float) =>
    val k = 0.7f
    rectSdf.biw = 4 * abs(sin(Time.current * k))
    rectSdf.bow = 7 * abs(sin(Time.current * k))
    // rectSdf.bow = 10 * abs(sin(Time.current * k)) + 5

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
      Image.resetCursor(window.windowId)
    }

    if (input.justReleased(KeyCode.escape)) {
      quit()
    }
  }

  run()
}
