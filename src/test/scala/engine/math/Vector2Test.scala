package engine.math
import org.scalatest.freespec.AnyFreeSpec
import engine.math.pi

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
      val vector = Vector2(1, 1)
      assert(vector.angle == pi / 4f)
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
      assert(Vector2.fromAngle(angle, length) nearEquals Vector2(-3, 0))
    }
    "should have correct from angle 2" in {
      val angle = (3f / 2f) * pi
      val length = 3f
      assert(Vector2.fromAngle(angle, length) nearEquals Vector2(0, -3))
    }
    "should have correct from angle 3" in {
      val angle = pi / 4
      assert(
        Vector2.fromAngle(angle, 1) nearEquals Vector2(.707107, .707107),
        "got " + Vector2.fromAngle(angle, 1)
      )
    }
  }
}
