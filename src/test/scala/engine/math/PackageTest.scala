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
}
