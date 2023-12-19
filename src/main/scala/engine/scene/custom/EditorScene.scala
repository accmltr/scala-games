// package engine.scene.custom

// import engine.scene.Scene
// import engine.Game
// import engine.input.KeyCode
// import java.awt.RenderingHints.Key
// import engine.rendering.shader.Shader
// import org.lwjgl.opengl.GL
// import org.lwjgl.opengl.GL11._
// import org.lwjgl.opengl.GL13._
// import org.lwjgl.opengl.GL15._
// import org.lwjgl.opengl.GL20._
// import org.lwjgl.opengl.GL30._

// import org.lwjgl.BufferUtils
// import engine.Time
// import engine.math.Vector3
// import engine.math.Vector2

// class EditorScene(game: Game) extends Scene(game) {

//   var imagePath = "src/resources/sample_image.png"

//   val quadRenderer = QuadRenderer(game)

//   override def init(): Unit = {
//     game.window.title = "Level Scene"
//     quadRenderer.init()
//   }

//   override def renderUpdate(delta: Float): Unit = {
//     quadRenderer.render()

//     // Update the clear color based on the time
//     val clearColor = Vector3(
//       (math.sin(Time.current) + 1.0) / 2.0, // Red component
//       (math.sin(Time.current + 2.0) + 1.0) / 2.0, // Green component
//       (math.sin(Time.current + 4.0) + 1.0) / 2.0 // Blue component
//     )

//     game.window.backgroundColor = clearColor

//     // Calculate the movement using a modulo operation on Time.current
//     val t =
//       Time.current % 5.0f // Change the divisor (5.0f) to adjust the speed of movement

//     // Update the translation vector to make the quad move
//     // quadRenderer.setTranslation(
//     //   (t / 5) * game.window.size.width, // X-axis
//     //   0.0f,
//     //   0.0f
//     // ) // Translate along the X-axis
//     // quadRenderer.setCameraPosition(
//     //   Vector2(
//     //     (t / 5 - 0.5) * game.window.size.width,
//     //     0.0f
//     //   )
//     // ) // Translate along the X-axis

//     // Render the quad using the QuadRenderer
//     quadRenderer.render()
//   }

//   override def physicsUpdate(delta: Float): Unit = {}

// }

// import java.io.File
// import java.io.IOException
// import javax.imageio.ImageIO
// import java.awt.image.BufferedImage

// private def compileShader(shaderType: Int, source: String): Int = {
//   val shaderId = glCreateShader(shaderType)
//   glShaderSource(shaderId, source)
//   glCompileShader(shaderId)
//   val status = glGetShaderi(shaderId, GL_COMPILE_STATUS)
//   if (status != GL_TRUE) {
//     val infoLog = glGetShaderInfoLog(shaderId)
//     throw new RuntimeException(s"Error compiling shader:\n$infoLog")
//   }
//   shaderId
// }

// private def createShaderProgram(
//     vertexShaderId: Int,
//     fragmentShaderId: Int
// ): Int = {
//   val programId = glCreateProgram()
//   glAttachShader(programId, vertexShaderId)
//   glAttachShader(programId, fragmentShaderId)
//   glLinkProgram(programId)
//   val status = glGetProgrami(programId, GL_LINK_STATUS)
//   if (status != GL_TRUE) {
//     val infoLog = glGetProgramInfoLog(programId)
//     throw new RuntimeException(s"Error linking shader program:\n$infoLog")
//   }
//   programId
// }

// def loadImage(path: String): Option[BufferedImage] = {
//   try {
//     val file = new File(path)
//     val image = ImageIO.read(file)
//     Some(image)
//   } catch {
//     case e: IOException =>
//       println(s"Error loading image: ${e.getMessage}")
//       None
//   }
// }

// def bufferedImageToByteArray(image: BufferedImage): Array[Byte] = {
//   val width = image.getWidth
//   val height = image.getHeight
//   val pixels = new Array[Int](width * height)
//   image.getRGB(0, 0, width, height, pixels, 0, width)
//   val bytes = new Array[Byte](width * height * 4)
//   for (i <- 0 until width * height) {
//     val pixel = pixels(i)
//     bytes(i * 4 + 0) = ((pixel >> 16) & 0xff).toByte // red
//     bytes(i * 4 + 1) = ((pixel >> 8) & 0xff).toByte // green
//     bytes(i * 4 + 2) = ((pixel >> 0) & 0xff).toByte // blue
//     bytes(i * 4 + 3) = ((pixel >> 24) & 0xff).toByte // alpha
//   }
//   bytes
// }

// import java.nio.ByteBuffer
// import engine.input.KeyCode

// private def createTexture(
//     imageData: Array[Byte],
//     width: Int,
//     height: Int
// ): Int = {
//   val textureId = glGenTextures()
//   glBindTexture(GL_TEXTURE_2D, textureId)
//   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
//   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
//   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
//   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

//   // Create a ByteBuffer from the imageData array
//   val buffer = ByteBuffer.allocateDirect(imageData.length)
//   buffer.put(imageData)
//   buffer.flip()

//   glTexImage2D(
//     GL_TEXTURE_2D,
//     0,
//     GL_RGBA,
//     width,
//     height,
//     0,
//     GL_RGBA,
//     GL_UNSIGNED_BYTE,
//     buffer
//   )

//   glBindTexture(GL_TEXTURE_2D, 0)
//   textureId
// }
