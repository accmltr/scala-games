package engine.math

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals

class Matrix3Spec extends AnyFlatSpec with Matchers {

  "Matrix3" should "multiplying IDENTITY with a Vector2 should not change it" in {
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

  it should "create a proper rotation matrix" in {
    val r = Matrix3.rotation(pi / 2f)
    val v2 = Vector2(10f, 5f)
    assertNearEquals(r * v2, Vector2(5.0f, -10.0f))
  }

  it should "create a proper scaling matrix" in {
    val s = Matrix3.scaling(2f, 3f)
    val s2 = Matrix3.scaling(Vector2(2f, 3f))
    val v2 = Vector2(10f, 5f)
    assertNearEquals(s * v2, Vector2(20f, 15f))
    assertNearEquals(s2 * v2, Vector2(20f, 15f))
  }

  it should "create a transform matrix with proper translation" in {
    val transform = Matrix3(translation = Vector2(3f, 2f))
    val v2 = Vector2(0f, 0f)
    assertNearEquals(Vector2(3f, 2f), transform * v2)
  }

  it should "create a transform matrix with proper rotation" in {
    val transform = Matrix3(rotation = -pi / 2f)
    val v2 = Vector2(10f, 5f)
    assertNearEquals(Vector2(-5.0f, 10.0f), transform * v2)
  }

  it should "create a transform matrix with proper scaling" in {
    val transform = Matrix3(scale = Vector2(2f, 3f))
    val v2 = Vector2(10f, 5f)
    assertNearEquals(Vector2(20f, 15f), transform * v2)
  }
}
