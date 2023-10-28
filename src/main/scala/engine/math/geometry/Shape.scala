package engine.math.geometry

import engine.math._
import org.joml
import scala.compiletime.ops.float
import scala.compiletime.ops.boolean
import scala.annotation.tailrec
import engine.math.NearEqualsable
import scala.Conversion

trait Shape {
  def toPolygon: Polygon

  /** @param point
    *   a point relative to the shape origin
    * @return
    */
  def contains(point: Vector2): Boolean

  /** @param amount
    * @return
    */
  def grow(amount: Float): Shape
  def scale(amount: Float): Shape
}

// Implicit Conversions
object Shape {
  given Conversion[Shape, Polygon] = _.toPolygon
}
