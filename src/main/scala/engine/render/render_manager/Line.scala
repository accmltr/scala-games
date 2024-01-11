package engine.render.render_manager

import engine.render.rendered_element.RenderedElement
import engine.render.rendered_element.RenderedMesh
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import engine.render.shader_classes.Shader
import engine.math.Vector3
import engine.math.Vector2
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

final case class Line() extends RenderManager {

  private var _layerMap: Map[Float, Map[Shader, List[RenderedMesh]]] = Map.empty

  private[render] def isEmpty = _layerMap.isEmpty

  private[render] override def +=(element: RenderedElement): Unit = {
    // TODO: Fix the generics
    element match {
      case e: RenderedMesh =>
        val shdrs = _layerMap.getOrElse(e.layer, Map.empty)
        val elmnts = shdrs.getOrElse(e.shader, Nil)

        if (elmnts.contains(e))
          throw new Exception("Element already added to manager")

        if shdrs.isEmpty
        then _layerMap = _layerMap + (e.layer -> (Map(e.shader -> (e :: Nil))))
        else if elmnts.isEmpty
        then
          _layerMap = (_layerMap - e.layer) + (e.layer -> (shdrs + (
            e.shader -> (e :: Nil)
          )))
        else
          _layerMap =
            (_layerMap - e.layer) + (e.layer -> ((shdrs - e.shader) + (
              e.shader -> (e :: elmnts)
            )))
      case _ =>
        throw new Exception("Invalid element type")
    }
  }

  private[render] override def -=(element: RenderedElement): Unit = {
    // TODO: Fix this mess
    element match {
      case e: RenderedMesh => {
        val shaderMap = _layerMap.getOrElse(e.layer, Map.empty)
        val elements = shaderMap.getOrElse(e.shader, Nil)

        // If element does not exist, throw an exception:
        if (!elements.contains(e))
          throw new IllegalArgumentException(
            "Trying to remove non-existant render element from render manager."
          )

        // Else, filter out the element
        val newElements = elements.filterNot(_ == e)
        val newShaderMap =
          if (newElements.isEmpty)
            shaderMap.filterNot(_._1 == e.shader)
          else
            shaderMap.filterNot(_._1 == e.shader) + (e.shader -> newElements)
        val newLayerMap =
          if (newShaderMap.isEmpty)
            _layerMap.filterNot(_._1 == e.layer)
          else
            _layerMap.filterNot(_._1 == e.layer) + (e.layer -> newShaderMap)

        _layerMap = newLayerMap
      }
      case _ => throw new Exception("Invalid element type")
    }
  }

  private[render] override def renderLayer(layer: Float): Unit = {

    _layerMap.get(layer) match
      case None =>
        return
      case Some(shaderMap) => {

        for
          (shader, elements) <- shaderMap
          e <- elements
        do {

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

          // Upload uniforms
          e.uploadUniforms()

          glBindVertexArray(vaoId)
          glDrawElements(
            GL_TRIANGLES,
            e.mesh.indices.length,
            GL_UNSIGNED_INT,
            0
          )
          glBindVertexArray(0)

        }
      }
  }
}
