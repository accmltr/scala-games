package engine.math.shapes

import engine.math.Vector2

/** An upright capsule shape, with its center at the origin, its flat sides
  * parallel to the y-axis.
  *
  * @param length
  *   The length of the capsule, including the semicircles.
  * @param girth
  *   The distance between the flat sides of the capsule.
  */
final case class Capsule(length: Float, girth: Float) extends Shape2D {

  if (length < 2 * girth)
    throw new IllegalArgumentException(
      "The length of the capsule must be at least twice its girth."
    )

  override def contains(point: Vector2): Boolean = {
    val inRect = Rectangle(length, girth).contains(point)
    val inTopSemiCircle = Circle(girth / 2).contains(
      point - Vector2(0, length / 2)
    )
    val inBottomSemiCircle = Circle(girth / 2).contains(
      point + Vector2(0, length / 2)
    )
    inRect || inTopSemiCircle || inBottomSemiCircle
  }

  override def scale(amount: Float): Shape2D =
    Capsule(length * amount, girth * amount)

  override def grow(amount: Float): Shape2D =
    Capsule(length + 2 * amount, girth + 2 * amount)

}
