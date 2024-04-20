package engine.math

import org.scalatest.freespec.AnyFreeSpec
import engine.test_utils.*

class math extends AnyFreeSpec {
  "nearEquals" - {
    "should be true for equal values" in {
      assertNearEquals(1f, 1f)
    }
    "should be true for near equal values" in {
      assertNearEquals(1.00000f, 1.00001f, 0.00001f)
      assertNearEquals(1.00001f, 1.00000f, 0.00001f)
    }
  }

  "inBounds" - {
    "should be true for values in wide bounds" in {
      assert(inBounds(1, 0, 2))
      assert(inBounds(1, 2, 0))
    }
    "should be false for values outside wide bounds" in {
      assert(!inBounds(3, 2, 0))
      assert(!inBounds(2.5, 2.6, 4))
    }
    "should be true for values in zero width bounds" in {
      assert(inBounds(1, 1, 1))
      assert(inBounds(-0.1, -0.1, -0.1))
    }
  }

  "angleInBounds" - {
    "should be true for angles in bounds" in {
      assertAngleInBounds(1f, 0f, 2f)
      assertAngleInBounds(1, 2, 0, true)

      assertNotAngleInBounds(1, 2, 0)
      assertAngleInBounds(2.5, 2, 0)

      assertAngleInBounds(1, 2, 0, true)
      assertNotAngleInBounds(2.5, 2, 0, true)

      assertAngleInBounds(2, 2, 0, true)
      assertNotAngleInBounds(2, 0, 2, true)
      assertNotAngleInBounds(2, 2, 0)
      assertAngleInBounds(2, 0, 2)
    }

    "should true for zero width bounds" in {
      assertAngleInBounds(1, 1, 1)
      assertAngleInBounds(1, 1, 1, true)
    }
  }

  "normalAngle" - {
    "should return angle in bounds" in {
      assert(normalAngle(-pi) == pi)
      assert(normalAngle(-pi / 2) == 3 * pi / 2)
      assert(normalAngle(-pi / 4) == 7 * pi / 4)
      assert(normalAngle(0) == 0)
      assert(normalAngle(pi / 4) == pi / 4)
      assert(normalAngle(pi / 2) == pi / 2)
      assert(normalAngle(pi) == pi)
      assert(normalAngle(2 * pi) == 0)
    }
  }
}
