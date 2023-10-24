package engine.math

/** Library for doing math with Floats instead of Doubles (like in scala.math),
  * and other useful math for making games.
  */
object Operations {
  def clamp(v: Float, min: Float, max: Float) = math.min(math.max(min, v), max)
  def sin(rad: Float): Float = math.sin(rad).toFloat
  def cos(rad: Float): Float = math.cos(rad).toFloat
  def tan(rad: Float): Float = math.tan(rad).toFloat
  def sqrt(n: Float): Float = math.sqrt(n).toFloat
}
