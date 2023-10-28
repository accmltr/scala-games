package engine.math.extended_primitives

import engine.test_utils.NearEqualsable

private[math] class ImplicitFloat(value: Float) extends NearEqualsable[Float] {

  override def nearEquals(other: Float, epsilon: Float = 0.0001f): Boolean = {
    engine.math.Operations.nearEquals(value, other, epsilon)
  }

}
