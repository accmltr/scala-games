package engine.test_utils

trait NearEqualsable[T] {
  def nearEquals(other: T, epsilon: Float): Boolean
}
