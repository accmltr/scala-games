package engine.math

import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals
import org.scalatest.freespec.AnyFreeSpec

class Matrix3Spec extends AnyFreeSpec with Matchers {

  "Matrix3" - {
    "multiplying IDENTITY with a Vector2 should not change it" in {
      val v2 = Vector2(3f, 7f)
      assertNearEquals(Matrix3.IDENTITY * v2, v2)
    }

    "create a propper translation matrix" in {
      val t = Matrix3.translation(3f, 2f)
      val t2 = Matrix3.translation(Vector2(3f, 2f))
      val v2 = Vector2(0f, 0f)
      assertNearEquals(t * v2, Vector2(3f, 2f))
      assertNearEquals(t2 * v2, Vector2(3f, 2f))
    }

    "create a propper rotation matrix" in {
      val r = Matrix3.rotation(pi / 2f)
      val v2 = Vector2(10f, 5f)
      assertNearEquals(r * v2, Vector2(5.0f, -10.0f))
    }

    "create a propper scaling matrix" in {
      val s = Matrix3.scaling(2f, 3f)
      val s2 = Matrix3.scaling(Vector2(2f, 3f))
      val v2 = Vector2(10f, 5f)
      assertNearEquals(s * v2, Vector2(20f, 15f))
      assertNearEquals(s2 * v2, Vector2(20f, 15f))
    }

    "create a transform matrix with propper translation" in {
      val transform = Matrix3(translation = Vector2(3f, 2f))
      val v2 = Vector2(0f, 0f)
      assertNearEquals(Vector2(3f, 2f), transform * v2)
    }

    "create a transform matrix with propper rotation" in {
      val transform = Matrix3(rotation = -pi / 2f)
      val v2 = Vector2(10f, 5f)
      assertNearEquals(Vector2(-5.0f, 10.0f), transform * v2)
    }

    "create a transform matrix with propper scaling" in {
      val transform = Matrix3(scale = Vector2(2f, 3f))
      val v2 = Vector2(10f, 5f)
      assertNearEquals(Vector2(20f, 15f), transform * v2)
    }
  }

  "Matrix3 constructor" - {
    "when creating a rotation matrix" - {
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
}
