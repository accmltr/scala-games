package lib.level

import org.scalatest.freespec.AnyFreeSpec
import engine.test_utils.assertNearEquals
import engine.math.given
import lib.level.Basic._

class BasicTest extends AnyFreeSpec {
  "expToLevel" - {
    "should be 1 for 0 exp" in {
      assert(expToLevel(0) == 1)
    }
    "should be 1.5 for 50 exp" in {
      assert(expToLevel(50) == 1.5)
    }
    "should be 1.99 for 99 exp" in {
      assertNearEquals(expToLevel(99), 1.99f)
    }
    "should be 2 for 100 exp" in {
      assert(expToLevel(100) == 2)
    }
    "should be 2.5 for 250 exp" in {
      assert(expToLevel(250) == 2.5)
    }
    "should be 3 for 400 exp" in {
      assert(expToLevel(400) == 3)
    }
  }
  "levelToExp" - {
    "should be 0 for level 1" in {
      assert(levelToExp(1) == 0)
    }
    "should be 50 for level 1.5" in {
      assert(levelToExp(1.5) == 50)
    }
    "should be 99 for level 1.99" in {
      assert(levelToExp(1.99) == 99)
    }
    "should be 100 for level 2" in {
      assert(levelToExp(2) == 100)
    }
    "should be 250 for level 2.5" in {
      assert(levelToExp(2.5) == 250)
    }
    "should be 400 for level 3" in {
      assert(levelToExp(3) == 400)
    }
    "should be 900 for level 4" in {
      assert(levelToExp(4) == 900)
    }
    "should be 1600 for level 5" in {
      assert(levelToExp(5) == 1600)
    }
    "should be 2500 for level 6" in {
      assert(levelToExp(6) == 2500)
    }
    "should be 3600 for level 7" in {
      assert(levelToExp(7) == 3600)
    }
    "should be 4900 for level 8" in {
      assert(levelToExp(8) == 4900)
    }
    "should be 6400 for level 9" in {
      assert(levelToExp(9) == 6400)
    }
    "should be 8100 for level 10" in {
      assert(levelToExp(10) == 8100)
    }
  }
  "conversion between exp and level" - {
    "should be consistent" in {
      for (exp <- 0 to 10000) {
        assertNearEquals(
          expToLevel(exp),
          expToLevel(levelToExp(expToLevel(exp)))
        )
      }
    }
  }
}
