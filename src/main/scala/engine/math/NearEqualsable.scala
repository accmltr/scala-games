package engine.math

/** Useful for comparing objects with floating point values that might not be
  * exactly equal.
  *
  * Extend this in your own class to make it functional with some of the engine
  * features, e.g. `test_utils.assertNearEquals()`.
  */
trait NearEqualsable[T] {
  def nearEquals(other: T, epsilon: Float): Boolean
}
