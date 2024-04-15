package engine.math.geometry

import engine.math.Vector2

final case class Polyline(points: List[Vector2]) {

  def count: Int = points.length
  def isEmpty = points.isEmpty

  def length: Float = {
    points
      .sliding(2)
      .foldLeft(0f)((acc, pair) => acc + pair(0).distanceTo(pair(1)))
  }

  def lengthSquared: Float = {
    points
      .sliding(2)
      .foldLeft(0f)((acc, pair) => acc + pair(0).distanceToSquared(pair(1)))
  }
}
