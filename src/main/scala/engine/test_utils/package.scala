package engine
import math.NearEqualsable
import engine.math.anglesEqual

package object test_utils {
  def assertNearEquals[T](
      o1: NearEqualsable[T],
      o2: T,
      epsilon: Float = 0.0001f,
      message: String = ""
  ): Unit = {
    assert(
      o1.nearEquals(o2, epsilon),
      s"$o1 did not equal $o2 " + message
    )
  }

  def assertNotNearEquals[T](
      o1: NearEqualsable[T],
      o2: T,
      epsilon: Float = 0.0001f,
      message: String = ""
  ): Unit = {
    assert(
      !o1.nearEquals(o2, epsilon),
      s"$o1 did equal $o2 " + message
    )
  }

  def assertAnglesEqual(
      a1: Float,
      a2: Float,
      epsilon: Float = 0.0001f,
      message: String = ""
  ): Unit = {
    assert(
      anglesEqual(a1, a2, epsilon),
      s"$a1 did not equal $a2 " + message
    )
  }
}
