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

  "inBounds" - {
    "should be true for values in bounds" in {
      assert(inBounds(1, 0, 2))
      assert(inBounds(1, 2, 0))
    }
  }

  "angleInBounds" - {
    "should be true for angles in bounds" in {
      assert(angleInBounds(1, 0, 2))
      assert(angleInBounds(1, 2, 0, true))

      assert(!angleInBounds(1, 2, 0))
      assert(angleInBounds(2.5, 2, 0))

      assert(angleInBounds(1, 2, 0, true))
      assert(!angleInBounds(2.5, 2, 0, true))

      assert(angleInBounds(2, 2, 0, true))
      assert(!angleInBounds(2, 0, 2, true))
      assert(!angleInBounds(2, 2, 0))
      assert(angleInBounds(2, 0, 2))
    }
  }
}
