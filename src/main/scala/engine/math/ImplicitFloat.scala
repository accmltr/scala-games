package engine.math

import engine.test_utils.NearEqualsable

private[math] class ImplicitFloat(value: Float) extends NearEqualsable[Float] {

  override def nearEquals(other: Float, epsilon: Float = 0.0001f): Boolean = {
    Operations.nearEquals(value, other, epsilon)
  }

}
