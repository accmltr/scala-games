package engine.math

trait NearEqualsable[T] {
  def nearEquals(other: T, epsilon: Float): Boolean
}
