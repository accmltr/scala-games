package engine.render.rendered_element

import engine.render.render_manager.RenderManager
import java.nio.{FloatBuffer, IntBuffer}

trait RenderedElement {

  /** Vertices of the shape, used to form triangles and render the shape to the
    * screen.
    */
  val vertices: FloatBuffer

  /** Indices of vertices that form triangles, used to render the shape.
    */
  val indices: IntBuffer

  def isManager(manager: RenderManager): Boolean
}

trait BatchRenderedElement extends RenderedElement {}

trait SelfRenderedElement extends RenderedElement {
  def render(): Unit
}
