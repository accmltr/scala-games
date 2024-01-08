package engine.math

import org.scalatest.freespec.AnyFreeSpec

class PackageTest extends AnyFreeSpec {
  "nearEquals" - {
    "should be true for equal values" in {
      assert(nearEquals(1, 1))
    }
    "should be true for near equal values" in {
      assert(nearEquals(1.00000, 1.00001, 0.00001))
      assert(nearEquals(1.00001, 1.00000, 0.00001))
    }
  }

  "angleInBounds" - {
    "should be true for angles in bounds" in {
      assert(angleInBounds(rad(45), rad(0), rad(90)))
      assert(angleInBounds(rad(45), rad(90), rad(0), true))
      assert(angleInBounds(rad(-315), rad(0), rad(90)))
      assert(angleInBounds(rad(-315), rad(0), rad(-270), true))
    }
  }
}
