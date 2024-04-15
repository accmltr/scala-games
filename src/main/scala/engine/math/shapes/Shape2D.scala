package engine.math.shapes

trait Shape2D extends Shape {
  def grow(amount: Float): Shape2D
  def scale(amount: Float): Shape2D
}
