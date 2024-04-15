package engine.math

import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals
import org.scalatest.freespec.AnyFreeSpec
import engine.test_utils.assertAnglesEqual

class Matrix3Test extends AnyFreeSpec with Matchers {

  "Matrix3" - {
    "when multiplying" - {
      "IDENTITY with" - {
        "Vector2" in {
          val v2 = Vector2(3f, 7f)
          assert(Matrix3.IDENTITY * v2 == v2)
        }
        "any Matrix3" in {
          val m = Matrix3(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f)
          assert(Matrix3.IDENTITY * m == m)
          val m2 = Matrix3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
          assert(Matrix3.IDENTITY * m2 == m2)
        }
      }
    }
    
    "create a proper translation matrix" in {
      val t = Matrix3.translation(3f, 2f)
      val t2 = Matrix3.translation(Vector2(3f, 2f))
      val v2 = Vector2(0f, 0f)
      assertNearEquals(t * v2, Vector2(3f, 2f))
      assertNearEquals(t2 * v2, Vector2(3f, 2f))
    }

    "create a proper rotation matrix" in {
      val r = Matrix3.rotation(pi / 2f)
      val v2 = Vector2(10f, 5f)
      assertNearEquals(r * v2, Vector2(-5.0f, 10.0f))
    }

    "create a proper scaling matrix" in {
      val s = Matrix3.scaling(2f, 3f)
      val s2 = Matrix3.scaling(Vector2(2f, 3f))
      val v2 = Vector2(10f, 5f)
      assertNearEquals(s * v2, Vector2(20f, 15f))
      assertNearEquals(s2 * v2, Vector2(20f, 15f))
    }

    "create a transform matrix with proper translation" in {
      val transform = Matrix3(translation = Vector2(3f, 2f))
      val v2 = Vector2(0f, 0f)
      assertNearEquals(Vector2(3f, 2f), transform * v2)
    }

    "create a transform matrix with proper rotation" in {
      val transform = Matrix3(rotation = pi / 2f)
      val v2 = Vector2(10f, 5f)
      assertNearEquals(Vector2(-5.0f, 10.0f), transform * v2)
    }

    "create a transform matrix with proper scaling" in {
      val transform = Matrix3(scale = Vector2(2f, 3f))
      val v2 = Vector2(10f, 5f)
      assertNearEquals(Vector2(20f, 15f), transform * v2)
      val transform2 = Matrix3(scale = Vector2(-7f, 2.1f))
      assertNearEquals(Vector2(-70f, 10.5f), transform2 * v2)
      val transform3 = Matrix3(scale = Vector2(7f, -2.1f))
      assertNearEquals(Vector2(70f, -10.5f), transform3 * v2)
      val transform4 = Matrix3(scale = Vector2(-7f, -2.1f))
      assertNearEquals(Vector2(-70f, -10.5f), transform4 * v2)
    }
  }

  "Matrix3 rotation matrix" - {
    "should rotate a Vector2 by the given rotation" in {
      assertNearEquals(Matrix3(rotation = pi) * Vector2(1, 0), Vector2(-1, 0))
      assertNearEquals(
        Matrix3(rotation = pi / 2f) * Vector2(1, 0),
        Vector2(0, 1)
      )
      assertNearEquals(
        Matrix3(rotation = pi / 4f) * Vector2(1, 0),
        Vector2(1 / Math.sqrt(2).toFloat, 1 / Math.sqrt(2).toFloat)
      )
      assertNearEquals(
        Matrix3(rotation = pi / 4f) * Vector2(0, 1),
        Vector2(-1 / Math.sqrt(2).toFloat, 1 / Math.sqrt(2).toFloat)
      )
    }
  }
}
