package engine.render.shape_renderer

import engine.math.shapes.Circle
import engine.math.*
import java.nio.FloatBuffer
import java.nio.IntBuffer
import org.lwjgl.BufferUtils

case class CircleRenderer(radius: Float, segments: Int = 32)
    extends ShapeRenderer {

  override val vertices: FloatBuffer = {
    val angle = (2 * pi) / segments
    val halfRadius = radius / 2
    val floatBuffer = BufferUtils.createFloatBuffer((segments + 1) * 2)
    floatBuffer.put(0)
    floatBuffer.put(0)
    println(s"Added vertex: 0, 0")
    for (i <- 0 until segments) {
      val x = halfRadius * cos(angle * i)
      val y = halfRadius * sin(angle * i)
      floatBuffer.put(x)
      floatBuffer.put(y)
    }
    floatBuffer.flip()
  }
  override val indices: IntBuffer = {
    val intBuffer = BufferUtils.createIntBuffer(segments * 3)
    for (i <- 1 to segments) {
      intBuffer.put(0)
      intBuffer.put(i)
      intBuffer.put(if (i == segments) 1 else i + 1)
    }
    intBuffer.flip()
  }
}

object CircleRenderer {
  def apply(circle: Circle, segments: Int): CircleRenderer = {
    CircleRenderer(circle.radius, segments)
  }
}
