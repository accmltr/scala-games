// package engine.render.rendered_element

// import engine.render.render_manager.RenderManager
// import engine.render.Color
// import engine.render.shader_classes.Shader
// import engine.math.Matrix3
// import engine.render.mesh.Mesh
// import engine.render.render_manager
// import engine.math.Vector2
// import engine.math.geometry.Line as geoLine
// import scala.util.boundary, boundary.break
// import engine.render.render_manager.LineRenderManager

// final case class LineRenderedElement(
//     val points: Array[Vector2],
//     val width: Float = 1,
//     override val shader: Shader = Shader(
//       "src/main/scala/engine/render/shaders/line/line.vert",
//       "src/main/scala/engine/render/shaders/line/line.frag"
//     ),
//     override val transform: Matrix3 = Matrix3.IDENTITY,
//     override val layer: Float = 0,
//     override val tint: Color = Color.WHITE,
//     override val uniforms: Map[String, Uniforms] = Map.empty
// ) extends RenderedElement(
//       shader
//       // shader = Shader(
//       //   "src/main/scala/general/shaders/experiment.vert",
//       //   "src/main/scala/general/shaders/experiment.frag"
//       // )
//       //   Shader(
//       //   "src/main/scala/engine/render/shaders/line/line.vert",
//       //   "src/main/scala/engine/render/shaders/line/line.frag"
//       // )
//     ) {

//   if points.length < 2 then
//     throw new Exception("Line must have at least 2 points")
//   if width <= 0 then throw new Exception("Line width must be positive")
//   if boundary[Boolean]:
//       for i <- 0 until points.length - 1
//       do
//         if points(i) == points(i + 1)
//         then break(true)
//       false
//   then throw new Exception("Line may not have 0 length segments")

//   override def newManager: RenderManager = LineRenderManager()

//   override def isManager(manager: RenderManager): Boolean =
//     manager match
//       case _: render_manager.LineRenderManager => true
//       case _                                   => false

//   def vertices: Array[Float] =
//     LineRenderedElement.pointsToRectVerts(points, width)

//   def indices: Array[Int] = LineRenderedElement.lineIndices(points.size)

// }

// object LineRenderedElement {
//   def pointsToRectVerts(points: Array[Vector2], width: Float): Array[Float] = {
//     (for i <- 0 until points.length - 1
//     yield
//       // Note: View 'p' to 'p + 1' as the forwards direction.
//       val line = geoLine(points(i), points(i + 1))
//       val offset = line.normal * width * 0.5f
//       val p1 = line.a + offset // bottom left
//       val p2 = line.a - offset // bottom right
//       val p3 = line.b + offset // top left
//       val p4 = line.b - offset // top right
//       Array(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
//     ).flatten.toArray
//   }

//   def lineIndices(pointCount: Int): Array[Int] = {
//     (0 until 4 * (pointCount - 1) by 4).foldLeft(Array[Int]())((acc, i) =>
//       acc ++ Array(i, i + 1, i + 2, i + 2, i + 1, i + 3)
//     )
//   }

// }
