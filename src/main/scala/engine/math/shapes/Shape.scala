package engine.math.shapes

import engine.math._
import org.joml
import scala.compiletime.ops.float
import scala.compiletime.ops.boolean
import scala.annotation.tailrec
import engine.math.NearEqualsable
import scala.Conversion

trait Shape {

  /** Returns true if the given point is contained within the shape. All shapes
    * have the origin at their center.
    */
  def contains(point: Vector2): Boolean

  /** Outwardly offsets the outline of the shape by the given amount.
    */
  def grow(amount: Float): Shape

  /** Scales the shape by the given amount.
    */
  def scale(amount: Float): Shape

}
