package engine.math

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals

class Matrix3Spec extends AnyFlatSpec with Matchers {

  "A Matrix3" should "multiplying IDENTITY with a Vector2 should not change it" in {
    val v2 = Vector2(3f, 7f)
    assertNearEquals(Matrix3.IDENTITY * v2, v2)
  }

  it should "create a proper translation matrix" in {
    val t = Matrix3.translation(3f, 2f)
    val t2 = Matrix3.translation(Vector2(3f, 2f))
    val v2 = Vector2(0f, 0f)
    assertNearEquals(t * v2, Vector2(3f, 2f))
    assertNearEquals(t2 * v2, Vector2(3f, 2f))
  }
}
