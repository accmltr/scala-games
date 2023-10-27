package engine

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
}
