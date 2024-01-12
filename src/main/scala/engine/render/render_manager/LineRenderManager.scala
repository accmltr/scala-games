package engine.render.render_manager

import engine.render.rendered_element.{RenderedElement, LineRenderedElement}
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

final case class LineRenderManager() extends RenderManager {

  private var _layerMap: Map[Float, Map[Shader, List[
    LineRenderedElement
  ]]] =
    Map.empty

  private[render] def isEmpty = _layerMap.isEmpty

  private[render] override def +=(element: RenderedElement): Unit = {
    // TODO: Fix the generics
    element match {
      case l: LineRenderedElement =>
        val shdrs = _layerMap.getOrElse(l.layer, Map.empty)
        val elmnts = shdrs.getOrElse(l.shader, Nil)

        if (elmnts.contains(l))
          throw new Exception("Element already added to manager")

        if shdrs.isEmpty
        then _layerMap = _layerMap + (l.layer -> (Map(l.shader -> (l :: Nil))))
        else if elmnts.isEmpty
        then
          _layerMap = (_layerMap - l.layer) + (l.layer -> (shdrs + (
            l.shader -> (l :: Nil)
          )))
        else
          _layerMap =
            (_layerMap - l.layer) + (l.layer -> ((shdrs - l.shader) + (
              l.shader -> (l :: elmnts)
            )))
      case _ =>
        throw new Exception("Invalid element type")
    }
  }

  private[render] override def -=(element: RenderedElement): Unit = {
    // TODO: Fix this mess
    element match {
      case l: LineRenderedElement => {
        val shaderMap = _layerMap.getOrElse(l.layer, Map.empty)
        val elements = shaderMap.getOrElse(l.shader, Nil)

        // If element does not exist, throw an exception:
        if (!elements.contains(l))
          throw new IllegalArgumentException(
            "Trying to remove non-existant render element from render manager."
          )

        // Else, filter out the element
        val newElements = elements.filterNot(_ == l)
        val newShaderMap =
          if (newElements.isEmpty)
            shaderMap.filterNot(_._1 == l.shader)
          else
            shaderMap.filterNot(_._1 == l.shader) + (l.shader -> newElements)
        val newLayerMap =
          if (newShaderMap.isEmpty)
            _layerMap.filterNot(_._1 == l.layer)
          else
            _layerMap.filterNot(_._1 == l.layer) + (l.layer -> newShaderMap)

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
          l <- elements
        do {

          // Create and bind a VAO
          val vaoId = glGenVertexArrays()
          glBindVertexArray(vaoId)

          // Create and bind a VBO for the vertices
          val vboId = glGenBuffers()
          glBindBuffer(GL_ARRAY_BUFFER, vboId)
          glBufferData(GL_ARRAY_BUFFER, l.vertices, GL_STATIC_DRAW)
          glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
          glEnableVertexAttribArray(0)

          // Create and bind a VBO for the indices
          val eboId = glGenBuffers()
          glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
          glBufferData(GL_ELEMENT_ARRAY_BUFFER, l.indices, GL_STATIC_DRAW)

          // Unbind the VAO
          glBindVertexArray(0)

          l.shader.use()

          // Upload uniforms
          l.uploadUniforms()

          glBindVertexArray(vaoId)
          glDrawElements(
            GL_TRIANGLES,
            l.indices.length,
            GL_UNSIGNED_INT,
            0
          )
          glBindVertexArray(0)

        }
      }
  }
}
