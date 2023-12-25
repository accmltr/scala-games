package engine.render.render_manager
import engine.render.rendered_element.RenderedElement
import engine.render.rendered_element.RenderedMesh
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

trait RenderManager {

  private[render] def +=(element: RenderedElement): Unit

  private[render] def -=(element: RenderedElement): Unit

  private[render] def renderLayer(layer: Float): Unit
}

final case class MeshRenderManager() extends RenderManager {

  private var _elements: List[RenderedMesh] = Nil

  private[render] override def +=(element: RenderedElement): Unit = {
    // TODO: Fix this mess
    element match {
      case e: RenderedMesh => _elements = e :: _elements
      case _               => throw new Exception("Invalid element type")
    }
  }

  private[render] override def -=(element: RenderedElement): Unit = {
    // TODO: Fix this mess
    element match {
      case e: RenderedMesh => _elements = _elements.filterNot(_ == e)
      case _               => throw new Exception("Invalid element type")
    }
  }

  private[render] override def renderLayer(layer: Float): Unit = {
    for (e <- _elements.filter(_.layer == layer)) {
      // Create and bind a VAO
      val vaoId = glGenVertexArrays()
      glBindVertexArray(vaoId)

      // Create and bind a VBO for the vertices
      val vboId = glGenBuffers()
      glBindBuffer(GL_ARRAY_BUFFER, vboId)
      glBufferData(GL_ARRAY_BUFFER, e.mesh.vertices, GL_STATIC_DRAW)
      glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
      glEnableVertexAttribArray(0)

      // Create and bind a VBO for the indices
      val eboId = glGenBuffers()
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, e.mesh.indices, GL_STATIC_DRAW)

      // Unbind the VAO
      glBindVertexArray(0)

      e.shader.use()
      glBindVertexArray(vaoId)
      glDrawElements(GL_TRIANGLES, e.mesh.indices.limit(), GL_UNSIGNED_INT, 0)
      glBindVertexArray(0)
    }
  }
}
