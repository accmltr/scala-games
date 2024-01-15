package engine.render.renderable

import engine.render.shader.Uniform
import engine.render.shader.Shader
import engine.render.Color
import engine.math.Matrix3
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import engine.render.window.Window
import engine.math.Vector2
import engine.math.geometry.Line
import scala.util.boundary, boundary.break

/** A renderable object. Main purpose is that it contains all the data needed to
  * render itself, with possible local state for optimization.
  *
  * @param uniforms
  *   A map of uniform names to uniform values.
  * @param shader
  *   The shader to use when rendering.
  */
trait Renderable(
    val window: Option[Window],
    val vertices: Array[Float],
    val indices: Array[Int],
    val uniforms: Map[String, Uniform] = Map.empty,
    val shader: Option[Shader],
    val layer: Float = 0,
    val tint: Color = Color.WHITE,
    val transform: Matrix3 = Matrix3.IDENTITY
) {

  // Ensure window is not None
  private val _window = window.match
    case None        => throw new Exception("No 'window' provided")
    case Some(value) => value

  // Ensure vertices and indices are not too short
  if vertices.length < 3
  then throw new Exception("Vertices must have at least 3 points")
  if indices.length < 3
  then throw new Exception("Indices must have at least 3 points")

  // Ensure shader is not None
  private val _shader = shader.match
    case None        => throw new Exception("No 'shader' provided")
    case Some(value) => value

  // Throw exceptions if layer, tint or transform are keys in uniforms
  if uniforms.contains("layer")
  then throw new Exception("Uniform name 'layer' is reserved")
  if uniforms.contains("tint")
  then throw new Exception("Uniform name 'tint' is reserved")
  if uniforms.contains("transform")
  then throw new Exception("Uniform name 'transform' is reserved")

  /** Creates and binds VAO and VBO, uses shader program, uploads uniforms and
    * finally tells OpenGL to render.
    */
  final def render(): Unit = {

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

    // Bind shader program
    _shader.use()

    // Upload uniforms
    _shader.uploadUniforms(uniforms)

    glBindVertexArray(vaoId)
    glDrawElements(
      GL_TRIANGLES,
      indices.length,
      GL_UNSIGNED_INT,
      0
    )
    glBindVertexArray(0)
  }
}

// trait BasicRenderProperties2D(
//     val layer: Float,
//     val tint: Color,
//     val transform: Matrix3
// )

// final case class Polygon private (
//     window: Window,
//     vertices: Array[Float],
//     indices: Array[Int],
//     uniforms: Map[String, Uniform],
//     shader: Shader,
//     layer: Float,
//     tint: Color,
//     transform: Matrix3
// ) extends Renderable(
//       window,
//       vertices,
//       indices,
//       uniforms,
//       Shader(
//         "src/main/scala/engine/render/shaders/default/default.vert",
//         "src/main/scala/engine/render/shaders/default/default.frag"
//       )
//     )
//     with BasicRenderProperties2D(
//       layer,
//       tint,
//       transform
//     ) {}

// object Polygon {
//   def apply(
//       window: Window,
//       points: Vector[Vector2],
//       uniforms: Map[String, Uniform],
//       shader: Shader
//   ): Polygon = {

//     val polygon = engine.math.shapes.Polygon(points)

//     val vertices = polygon.points
//       .foldLeft(Array[Float]())((acc, point) =>
//         acc ++ Array[Float](point.x, point.y)
//       )

//     def lineFromIndices(i: (Int, Int)): Line = {
//       Line(polygon.points(i._1), polygon.points(i._2))
//     }

//     def linesEq(l1: (Int, Int), l2: (Int, Int)): Boolean = {
//       l1 == l2 || l1 == (l2._2, l2._1)
//     }

//     def isLineInTri(l: (Int, Int), t: (Int, Int, Int)): Boolean = {
//       linesEq(l, (t._1, t._2)) ||
//       linesEq(l, (t._2, t._3)) ||
//       linesEq(l, (t._3, t._1))
//     }

//     def trisEq(t1: (Int, Int, Int), t2: (Int, Int, Int)): Boolean = {
//       isLineInTri((t1._1, t1._2), t2) &&
//       isLineInTri((t1._2, t1._3), t2) &&
//       isLineInTri((t1._3, t1._1), t2)
//     }

//     def overlapsWithLines(
//         l: (Int, Int),
//         lines: List[(Int, Int)] = (0 until polygon.points.length)
//           .map(index =>
//             (
//               index,
//               if index + 1 == polygon.points.length then 0 else index + 1
//             )
//           )
//           .toList
//     ): Boolean = {
//       boundary[Boolean]:
//         for cl <- lines
//         do
//           val l1 = lineFromIndices(l)
//           val l2 = lineFromIndices(cl)
//           if l1.overlaps(l2, false)
//           then break(true)
//         false
//     }

//     def overlapsWithTris(
//         l: (Int, Int),
//         triangles: List[(Int, Int, Int)]
//     ): Boolean = {
//       val line = lineFromIndices(l)
//       boundary[Boolean]:
//         for
//           t <- triangles
//           tl <- List((t._1, t._2), (t._2, t._3), (t._3, t._1))
//             .map(lineFromIndices)
//         do
//           if line.overlaps(tl, false)
//           then break(true)
//         false
//     }

//     /** @param i
//       *   Index of outline point of polygon in question.
//       * @param to
//       *   New point to create line in question.
//       * @return
//       *   `true` when line points into the polygon. (Does not check if line
//       *   strikes through the polygon's other side.)
//       */
//     def angledInwards(i: Int, p: Int): Boolean = {

//       val ai =
//         if (i - 1) == -1
//         then polygon.points.size - 1
//         else i - 1
//       val bi =
//         if (i + 1) == polygon.points.size
//         then 0
//         else i + 1

//       val a = lineFromIndices(i, ai).angle
//       val k = lineFromIndices(i, p).angle
//       val b = lineFromIndices(i, bi).angle

//       engine.math.angleInBounds(
//         k,
//         b,
//         a,
//         polygon.isClockwise
//       )

//     }

//     def buildIndices(
//         remaining: List[(Int, Int)],
//         triangles: List[(Int, Int, Int)]
//     ): Array[Int] = {

//       if remaining.isEmpty
//       then
//         triangles.foldLeft(Array[Int]())((acc, t) =>
//           acc ++ Array[Int](t._1, t._2, t._3)
//         )
//       else
//         val li = remaining.head
//         val l = lineFromIndices(li)

//         boundary[Array[Int]]:
//           // Try to form a valid triangle
//           for p <- 0 until polygon.points.length if !(p == li._1 || p == li._2)
//           do
//             // Useful Locals
//             val l1i = (li._1, p)
//             val l2i = (li._2, p)
//             val l1 = lineFromIndices(l1i)
//             val l2 = lineFromIndices(l2i)
//             val tri = (li._1, li._2, p)

//             val triExists = !triangles.forall(!trisEq(tri, _))

//             if triExists
//             then break(buildIndices(remaining.tail, triangles))
//             else

//               // Check if lines already exist within 'triangles' or 'lines'
//               val l1Exists = {
//                 val existsInLines = !remaining.forall(!linesEq(l1i, _))
//                 val existsInTris = !triangles.forall(!isLineInTri(l1i, _))
//                 existsInLines || existsInTris
//               }
//               val l2Exists = {
//                 val existsInLines = !remaining.forall(!linesEq(l2i, _))
//                 val existsInTris = !triangles.forall(!isLineInTri(l2i, _))
//                 existsInLines || existsInTris
//               }

//               // Validity checks
//               val l1Valid =
//                 if l1Exists
//                 then true
//                 else
//                   !overlapsWithTris(l1i, triangles) &&
//                   !overlapsWithLines(l1i, remaining) &&
//                   angledInwards(li._1, p)
//               val l2Valid =
//                 if l2Exists
//                 then true
//                 else
//                   !overlapsWithTris(l2i, triangles) &&
//                   !overlapsWithLines(l2i, remaining) &&
//                   angledInwards(li._2, p)

//               // Valid recursion
//               if l1Valid && l2Valid
//               then break(buildIndices(remaining.tail, tri :: triangles))

//           // Throw and error if none found
//           throw new Exception("Polygon could not be triangulated")
//     }

//     val indices = buildIndices(
//       (0 until polygon.points.length)
//         .map(index =>
//           (index, if index + 1 == polygon.points.length then 0 else index + 1)
//         )
//         .toList,
//       Nil
//     )

//     new Polygon(window, vertices, indices, uniforms, shader)
//   }
// }
