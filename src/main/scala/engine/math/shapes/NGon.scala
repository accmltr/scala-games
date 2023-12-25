package engine.math.shapes

import engine.math.Vector2
import scala.util.boundary, boundary.break

final case class NGon(radius: Float, points: Int) extends Shape2D {

  override def contains(point: Vector2): Boolean = {
    // Todo: Test this method
    val center = Vector2.zero // Assuming the center of the NGon is at (0, 0)
    val angleStep = 2 * Math.PI / points
    boundary[Boolean]:
      for (i <- 0 until points) {
        val angle = i * angleStep
        val vertex = Vector2(
          center.x + radius * Math.cos(angle),
          center.y + radius * Math.sin(angle)
        )
        if (point.distanceSquared(vertex) < radius * radius) {
          break(true)
        }
      }
      break(false)
  }

  override def scale(amount: Float): NGon = {
    NGon(radius * amount, points)
  }

  override def grow(amount: Float): NGon = {
    // Todo: Implement this method
    ???
  }

}
