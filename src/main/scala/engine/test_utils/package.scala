package engine
import math.NearEqualsable
import engine.math.*

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

  def assertAngleInBounds(
      value: Float,
      a: Float,
      b: Float,
      clockwise: Boolean = false,
      message: String = ""
  ): Unit = {
    assert(
      angleInBounds(value, a, b, clockwise),
      s"$value was not in bounds of $a and $b, when clockwise $clockwise" + message
    )
  }

  def assertNotAngleInBounds(
      value: Float,
      a: Float,
      b: Float,
      clockwise: Boolean = false,
      message: String = ""
  ): Unit = {
    assert(
      !angleInBounds(value, a, b, clockwise),
      s"$value was in bounds of $a and $b, when clockwise $clockwise" + message
    )
  }

  def assertAnglesEqual(
      a1: Float,
      a2: Float,
      epsilon: Float = 0.0001f,
      message: String = ""
  ): Unit = {
    assert(
      anglesEqual(a1, a2, epsilon), {
        val a1r = "%.11f".format(a1 / pi)
        val a2r = "%.11f".format(a2 / pi)
        s"${a1r}Pi did not equal ${a2r}Pi " + message
      }
    )
  }
}
