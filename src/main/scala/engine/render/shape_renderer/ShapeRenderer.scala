package engine.render.shape_renderer

import engine.math.Vector2
import java.nio.FloatBuffer
import java.nio.IntBuffer

trait ShapeRenderer {

  /** Vertices of the shape, used to form triangles and render the shape to the
    * screen.
    */
  val vertices: FloatBuffer

  /** Indices of vertices that form triangles, used to render the shape.
    */
  val indices: IntBuffer
}
