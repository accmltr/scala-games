package engine.math
import org.scalatest.freespec.AnyFreeSpec
import engine.math.pi
import engine.test_utils.assertNearEquals

class Vector2Test extends AnyFreeSpec {
  "Vector2" - {
    "should have correct length" in {
      val vector = Vector2(3, 4)
      assert(vector.length == 5)
    }
    "should have correct length squared" in {
      val vector = Vector2(3, 4)
      assert(vector.lengthSquared == 25)
    }
    "should have correct angle" in {
      assert(Vector2(1, 1).angle == pi / 4f)
      assert(Vector2(1, 0).angle == 0f)
      assert(Vector2(1, -1).angle == (7f / 4f) * pi)
      assert(Vector2(0, 1).angle == pi / 2f)
      assert(Vector2(0, -1).angle == (3f / 2f) * pi)
      assert(Vector2(-1, 1).angle == (3f / 4f) * pi)
    }
    "should have correct angle between" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(1, -1)
      assert(vector1.angleBetween(vector2) == pi / 2f)
    }
    "should have correct angle between 2" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(-1, 1)
      assert(vector1.angleBetween(vector2) == pi / 2f)
    }
    "should have correct angle between 3" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(-1, -1)
      assert(vector1.angleBetween(vector2) == pi)
    }
    "should have correct angle between 4" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(1, 1)
      assert(vector1.angleBetween(vector2) == 0f)
    }
    "should have correct angle between 5" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(0, 1)
      assert(vector1.angleBetween(vector2) == pi / 4f)
    }
    "should have correct angle between 6" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(1, 0)
      assert(vector1.angleBetween(vector2) == pi / 4f)
    }
    "should have correct angle between 7" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(0, -1)
      assert(vector1.angleBetween(vector2) == (3f / 4f) * pi)
    }
    "should have correct angle between 8" in {
      val vector1 = Vector2(1, 1)
      val vector2 = Vector2(-1, 0)
      assert(vector1.angleBetween(vector2) == (3f / 4f) * pi)
    }
    "should have correct from angle 1" in {
      val angle = pi
      val length = 3f
      assertNearEquals(Vector2.fromAngle(angle, length), Vector2(-3, 0))
    }
    "should have correct from angle 2" in {
      val angle = (3f / 2f) * pi
      val length = 3f
      assertNearEquals(Vector2.fromAngle(angle, length), Vector2(0, -3))
    }
    "should have correct from angle 3" in {
      val angle = pi / 4
      assertNearEquals(Vector2.fromAngle(angle, 1), Vector2(.707107, .707107))
    }
    "should normalize" in {
      val vector = Vector2(3, 4)
      assertNearEquals(vector.normalize, Vector2(3f / 5f, 4f / 5f))
    }
    "should normalize without exception" in {
      val vector = Vector2(0, 0)
      assert(vector.normalize == Vector2.zero)
    }
    "should rotate" in {
      val vector = Vector2(1, 0)
      assertNearEquals(vector.rotated(pi / 2f), Vector2(0, 1))
    }
    "should nearEquals" in {
      val vector1 = Vector2(0, .5)
      val vector2 = Vector2(0, 0)
      assertNearEquals(vector1, vector2, .50001f)
    }
  }

  "Vector2.angle" - {
    "should return correct angle" in {
      assert(anglesEqual(Vector2(1, 0).angle, 0f))
      assert(anglesEqual(Vector2(1, 1).angle, pi / 4f))
      assert(anglesEqual(Vector2(0, 1).angle, pi / 2f))
      assert(anglesEqual(Vector2(-1, 1).angle, (3f / 4f) * pi))
      assert(anglesEqual(Vector2(-1, 0).angle, pi))
      assert(anglesEqual(Vector2(-1, -1).angle, (5f / 4f) * pi))
      assert(anglesEqual(Vector2(0, -1).angle, (3f / 2f) * pi))
      assert(anglesEqual(Vector2(1, -1).angle, (7f / 4f) * pi))
    }
  }

  "Vector2.fromAngle factory method" - {
    "should return correct vector" in {
      assert(Vector2.fromAngle(0, 1) nearEquals Vector2(1, 0))
      assert(
        Vector2.fromAngle(pi / 4, 1) nearEquals Vector2(.707107f, .707107f)
      )
      assert(Vector2.fromAngle(pi / 2, 1) nearEquals Vector2(0, 1))
      assert(
        Vector2.fromAngle((3f / 4f) * pi, 1) nearEquals Vector2(
          -.707107f,
          .707107f
        )
      )
      assert(Vector2.fromAngle(pi, 1) nearEquals Vector2(-1, 0))
      assert(
        Vector2.fromAngle((5f / 4f) * pi, 1) nearEquals Vector2(
          -.707107f,
          -.707107f
        )
      )
      assert(Vector2.fromAngle((3f / 2f) * pi, 1) nearEquals Vector2(0, -1))
      assert(
        Vector2.fromAngle((7f / 4f) * pi, 1) nearEquals Vector2(
          .707107f,
          -.707107f
        )
      )
      assert(Vector2.fromAngle(2 * pi) nearEquals Vector2(1, 0))
      assert(Vector2.fromAngle(-pi) nearEquals Vector2(-1, 0))
      assert(Vector2.fromAngle(-pi / 2) nearEquals Vector2(0, -1))
      assert(Vector2.fromAngle(-pi / 4) nearEquals Vector2(.707107f, -.707107f))
      assert(Vector2.fromAngle(-2 * pi) nearEquals Vector2(1, 0))

    }
  }

  "Vector2.angleTo" - {
    "should return correct angle" in {
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(1, 0)), 0f))
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(1, 1)), pi / 4f))
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(0, 1)), pi / 2f))
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(-1, 1)), (3f / 4f) * pi))
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(-1, 0)), pi))
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(-1, -1)), (5f / 4f) * pi))
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(0, -1)), (3f / 2f) * pi))
      assert(anglesEqual(Vector2.zero.angleTo(Vector2(1, -1)), (7f / 4f) * pi))
    }
  }
}
