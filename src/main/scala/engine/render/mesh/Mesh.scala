package engine.render.mesh

import engine.math.shapes.Polygon
import org.lwjgl.BufferUtils
import engine.math.shapes.*
import engine.math.*
import java.nio.{IntBuffer, FloatBuffer}

final case class Mesh(vertices: FloatBuffer, indices: IntBuffer)

object Mesh {
  def apply(circle: Circle, segments: Int): Mesh = {
    val angle = (2 * pi) / segments
    val halfRadius = circle.radius / 2

    val vertices = BufferUtils.createFloatBuffer((segments + 1) * 2)
    vertices.put(0)
    vertices.put(0)
    println(s"Added vertex: 0, 0")
    for (i <- 0 until segments) {
      val x = halfRadius * cos(angle * i)
      val y = halfRadius * sin(angle * i)
      vertices.put(x)
      vertices.put(y)
    }
    vertices.flip()

    val indices = BufferUtils.createIntBuffer(segments * 3)
    for (i <- 1 to segments) {
      indices.put(0)
      indices.put(i)
      indices.put(if (i == segments) 1 else i + 1)
    }
    indices.flip()
    Mesh(vertices, indices)
  }

  def apply(rectangle: Rectangle): Mesh = {
    val vertices = BufferUtils.createFloatBuffer(8)
    vertices.put(0)
    vertices.put(0)
    vertices.put(rectangle.width)
    vertices.put(0)
    vertices.put(rectangle.width)
    vertices.put(rectangle.height)
    vertices.put(0)
    vertices.put(rectangle.height)
    vertices.flip()

    val indices = BufferUtils.createIntBuffer(6)
    indices.put(0)
    indices.put(1)
    indices.put(2)
    indices.put(2)
    indices.put(3)
    indices.put(0)
    indices.flip()
    Mesh(vertices, indices)
  }

  def apply(polygon: Polygon): Mesh = {
    val vertices = BufferUtils.createFloatBuffer(polygon.points.length * 2)
    for (point <- polygon.points) {
      vertices.put(point.x)
      vertices.put(point.y)
    }
    vertices.flip()

    val indices = BufferUtils.createIntBuffer(polygon.points.length * 3)
    for (i <- 1 until polygon.points.length - 1) {
      indices.put(0)
      indices.put(i)
      indices.put(i + 1)
    }
    indices.flip()
    Mesh(vertices, indices)
  }

  def apply(ngon: NGon): Mesh = {
    val angle = (2 * pi) / ngon.points
    val halfRadius = ngon.radius / 2

    val vertices = BufferUtils.createFloatBuffer((ngon.points + 1) * 2)
    vertices.put(0)
    vertices.put(0)
    println(s"Added vertex: 0, 0")
    for (i <- 0 until ngon.points) {
      val x = halfRadius * cos(angle * i)
      val y = halfRadius * sin(angle * i)
      vertices.put(x)
      vertices.put(y)
    }
    vertices.flip()

    val indices = BufferUtils.createIntBuffer(ngon.points * 3)
    for (i <- 1 to ngon.points) {
      indices.put(0)
      indices.put(i)
      indices.put(if (i == ngon.points) 1 else i + 1)
    }
    indices.flip()
    Mesh(vertices, indices)
  }
}
