package engine.math.shapes

import engine.math.Vector2
import scala.util.boundary, boundary.break

final case class NGon(radius: Float, segments: Int) extends Shape2D {

  override def contains(point: Vector2): Boolean = {
    // Todo: Test this method
    val center = Vector2.zero // Assuming the center of the NGon is at (0, 0)
    val angleStep = 2 * Math.PI / segments
    boundary[Boolean]:
      for (i <- 0 until segments) {
        val angle = i * angleStep
        val vertex = Vector2(
          center.x + radius * Math.cos(angle),
          center.y + radius * Math.sin(angle)
        )
        if (point.distanceToSquared(vertex) < radius * radius) {
          break(true)
        }
      }
      break(false)
  }

  override def scale(amount: Float): NGon = {
    NGon(radius * amount, segments)
  }

  override def grow(amount: Float): NGon = {
    NGon(radius + amount, segments)
  }

}
