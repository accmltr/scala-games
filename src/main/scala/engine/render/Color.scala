package engine.render

import engine.math.Vector3

final case class Color(
    r: Float = 1.0f,
    g: Float = 1.0f,
    b: Float = 1.0f,
    a: Float = 1.0f
)

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
