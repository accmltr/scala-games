package engine.render

import engine.math.Vector3
import engine.math.*

final case class Color(
    r: Float = 1.0f,
    g: Float = 1.0f,
    b: Float = 1.0f,
    a: Float = 1.0f
) {

  if !inBounds(r, 0, 1)
  then throw IllegalArgumentException("'r' out of bounds: [0,1]")
  if !inBounds(g, 0, 1)
  then throw IllegalArgumentException("'g' out of bounds: [0,1]")
  if !inBounds(b, 0, 1)
  then throw IllegalArgumentException("'b' out of bounds: [0,1]")
  if !inBounds(a, 0, 1)
  then throw IllegalArgumentException("'a' out of bounds: [0,1]")

  def *(f: Float | Int | Double): Color =
    f match
      case i: Int =>
        Color(
          clamp(i.toFloat * r, 0, 1),
          clamp(i.toFloat * g, 0, 1),
          clamp(i.toFloat * b, 0, 1)
        )
      case d: Double =>
        Color(
          clamp(d.toFloat * r, 0, 1),
          clamp(d.toFloat * g, 0, 1),
          clamp(d.toFloat * b, 0, 1)
        )
      case f: Float =>
        Color(clamp(f * r, 0, 1), clamp(f * g, 0, 1), clamp(f * b, 0, 1))

  def *(other: Color): Color =
    Color(
      clamp(r * other.r, 0, 1),
      clamp(g * other.g, 0, 1),
      clamp(b * other.b, 0, 1),
      clamp(a * other.a, 0, 1)
    )

  def +(other: Color): Color =
    Color(
      clamp(r + other.r, 0f, 1f),
      clamp(g + other.g, 0f, 1f),
      clamp(b + other.b, 0f, 1f),
      clamp(a + other.a, 0f, 1f)
    )

  def -(other: Color): Color =
    Color(
      clamp(r - other.r, 0f, 1f),
      clamp(g - other.g, 0f, 1f),
      clamp(b - other.b, 0f, 1f),
      clamp(a - other.a, 0f, 1f)
    )
}

object Color {
  val WHITE: Color = Color()
  val BLACK: Color = Color(0.0f, 0.0f, 0.0f, 1.0f)
  val RED: Color = Color(1.0f, 0.0f, 0.0f, 1.0f)
  val GREEN: Color = Color(0.0f, 1.0f, 0.0f, 1.0f)
  val BLUE: Color = Color(0.0f, 0.0f, 1.0f, 1.0f)
  val YELLOW: Color = Color(1.0f, 1.0f, 0.0f, 1.0f)
  val CYAN: Color = Color(0.0f, 1.0f, 1.0f, 1.0f)
  val MAGENTA: Color = Color(1.0f, 0.0f, 1.0f, 1.0f)
  val GRAY: Color = Color(0.5f, 0.5f, 0.5f, 1.0f)
  val DARK_GRAY: Color = Color(0.3f, 0.3f, 0.3f, 1.0f)
  val LIGHT_GRAY: Color = Color(0.7f, 0.7f, 0.7f, 1.0f)
  val ORANGE: Color = Color(1.0f, 0.5f, 0.0f, 1.0f)
  val PINK: Color = Color(1.0f, 0.68f, 0.68f, 1.0f)
  val CLEAR: Color = Color(0.0f, 0.0f, 0.0f, 0.0f)

  def apply(hex: String): Color = {
    val r = Integer.parseInt(hex.substring(1, 3), 16)
    val g = Integer.parseInt(hex.substring(3, 5), 16)
    val b = Integer.parseInt(hex.substring(5, 7), 16)
    val a = if (hex.length == 9) {
      Integer.parseInt(hex.substring(7, 9), 16)
    } else {
      255
    }
    new Color(r, g, b, a)
  }

  def apply(rgb: Vector3, a: Float): Color = {
    new Color(rgb.x, rgb.y, rgb.z, a)
  }

  def apply(rgb: Vector3): Color = {
    new Color(rgb.x, rgb.y, rgb.z, 1.0f)
  }

  def apply(shade: Float) = {
    new Color(shade, shade, shade, 1.0f)
  }
}
