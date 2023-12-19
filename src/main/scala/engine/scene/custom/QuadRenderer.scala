// package engine.scene.custom

// import org.lwjgl.opengl.GL11._
// import org.lwjgl.opengl.GL13._
// import org.lwjgl.opengl.GL15._
// import org.lwjgl.opengl.GL20._
// import org.lwjgl.opengl.GL30._
// import org.lwjgl.BufferUtils
// import engine.math.Vector3
// import engine.rendering.shader.Shader
// import org.joml.Matrix4f
// import engine.rendering.Camera2D
// import engine.math.Vector2
// import engine.math.Vector2Implicits.given
// import engine.Game
// import engine.Time
// import engine.rendering.Texture
// import engine.rendering.Camera2D
// import engine.rendering.window.Window
// import engine.rendering.window.ScreenSizeImplicits.given

// class QuadRenderer(game: Game) {

//   private val defaultShader: Shader = Shader(
//     "src/main/scala/engine/templates/shaders/default.glsl"
//   )
//   private val camera: Camera2D = Camera2D(
//     projectionSize = game.window.size,
//     position = Vector2(0, -4.5)
//   )
//   val imagePath = "src/resources/sample_image.png"
//   val texture = Texture(imagePath)

//   def setCameraPosition(position: Vector2): Unit =
//     camera.position = position

//   private val vertices = Array[Float](
//     // Quad vertices
//     0f, 1f, 0.0f, // Top-left
//     1f, 1f, 0.0f, // Top-right
//     1f, 0f, 0.0f, // Bottom-right
//     0f, 0f, 0.0f // Bottom-left
//   ).map(_ * game.window.size.height)
//   private val vertexColors = Array[Float](
//     // Quad colors
//     1.0f, 0.0f, 0.0f, 1.0f, // Top-left
//     0.0f, 1.0f, 0.0f, 1.0f, // Top-right
//     0.0f, 0.0f, 1.0f, 1.0f, // Bottom-right
//     1.0f, 1.0f, 1.0f, 1.0f // Bottom-left
//   )
//   private val vertexArray = Array[Float](
//     vertices(0),
//     vertices(1),
//     vertices(2),
//     vertexColors(0),
//     vertexColors(1),
//     vertexColors(2),
//     vertexColors(3),
//     1,
//     1, // Top-left
//     vertices(3),
//     vertices(4),
//     vertices(5),
//     vertexColors(4),
//     vertexColors(5),
//     vertexColors(6),
//     vertexColors(7),
//     0,
//     1, // Top-right
//     vertices(6),
//     vertices(7),
//     vertices(8),
//     vertexColors(8),
//     vertexColors(9),
//     vertexColors(10),
//     vertexColors(11),
//     0,
//     0, // Bottom-right
//     vertices(9),
//     vertices(10),
//     vertices(11),
//     vertexColors(12),
//     vertexColors(13),
//     vertexColors(14),
//     vertexColors(15),
//     1,
//     0 // Bottom-left
//   )

//   private val elements = Array[Int](
//     0, 1, 2, // Top-left triangle
//     2, 3, 0 // Bottom-right triangle
//   )

//   private var shaderProgramID: Int = 0
//   private var vaoId: Int = 0
//   private var vboId: Int = 0

//   private var translation = Vector3(0.0f, 0.0f, 0.0f)

//   def init(): Unit = {
//     texture.init()
//     println("Texture: " + texture)
//     defaultShader.compile()

//     vaoId = glGenVertexArrays()
//     glBindVertexArray(vaoId)

//     val vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length)
//     vertexBuffer.put(vertexArray).flip()

//     vboId = glGenBuffers()
//     glBindBuffer(GL_ARRAY_BUFFER, vboId)
//     glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

//     // Create the indices and select it (bind) - INDICES
//     val elementBuffer = BufferUtils.createIntBuffer(elements.length)
//     elementBuffer.put(elements).flip()

//     val eboId = glGenBuffers()
//     glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
//     glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW)

//     // Define the structure of the data - VERTICES
//     val floatSize = 4
//     val positionSize = 3
//     val colorSize = 4
//     val uvSize = 2
//     val vertexSizeBytes = (positionSize + colorSize + uvSize) * floatSize

//     glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0)
//     glEnableVertexAttribArray(0)

//     glVertexAttribPointer(
//       1,
//       colorSize,
//       GL_FLOAT,
//       false,
//       vertexSizeBytes,
//       positionSize * floatSize
//     )
//     glEnableVertexAttribArray(1)

//     glVertexAttribPointer(
//       2,
//       uvSize,
//       GL_FLOAT,
//       false,
//       vertexSizeBytes,
//       (positionSize + colorSize) * floatSize
//     )
//     glEnableVertexAttribArray(2)

//   }

//   def render(): Unit = {

//     defaultShader.use()

//     // Upload texture
//     defaultShader.uploadTexture("uTexture", 0)
//     glActiveTexture(GL_TEXTURE0)
//     texture.bind()

//     defaultShader.uploadMat4f("uProjection", camera.projectionMatrix)
//     defaultShader.uploadMat4f("uView", camera.viewMatrix)
//     defaultShader.uploadFloat("uTime", Time.current)
//     defaultShader.uploadVec2f("uResolution", game.window.size)
//     defaultShader.uploadVec2f("uMouse", game.input.mousePosition)

//     // Bind the vertex array object (VAO) containing the quad vertices
//     glBindVertexArray(vaoId)

//     // Apply translation to the quad vertices
//     val transformedVertexArray = new Array[Float](vertexArray.length)
//     var i = 0
//     val positionSize = 3
//     val colorSize = 4
//     while (i < vertexArray.length) {
//       if (i % (positionSize + colorSize) == 0) {
//         // Update position vertices
//         transformedVertexArray(i) = vertexArray(i) + translation.x
//       } else {
//         // Update color vertices
//         transformedVertexArray(i) = vertexArray(i)
//       }
//       i += 1
//     }

//     // Update the vertex buffer with the transformed vertices
//     val vertexBuffer =
//       BufferUtils.createFloatBuffer(transformedVertexArray.length)
//     vertexBuffer.put(transformedVertexArray).flip()
//     glBindBuffer(GL_ARRAY_BUFFER, vboId)
//     glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

//     // Draw the quad
//     glDrawElements(GL_TRIANGLES, elements.length, GL_UNSIGNED_INT, 0)

//     // Unbind the vertex array object and shader program
//     glBindVertexArray(0)

//     defaultShader.detach()
//   }

//   def cleanup(): Unit = {
//     glDeleteBuffers(vboId)
//     glDeleteVertexArrays(vaoId)
//     glDeleteProgram(shaderProgramID)
//   }

//   def setTranslation(x: Float, y: Float, z: Float): Unit =
//     translation = Vector3(x, y, z)
// }
