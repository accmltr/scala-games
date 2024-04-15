package lib.veiled_stats

import org.scalatest.freespec.AnyFreeSpec

class StatSheetTest extends AnyFreeSpec {
  "StatSheet" - {
    "should be empty by default" in {
      assert(StatSheet().isEmpty)
    }
    "should be empty if all stats are 0" in {
      assert(StatSheet(0, 0, 0, 0, 0).isEmpty)
    }
    "should not be empty if any stat is non-zero" in {
      assert(!StatSheet(0, 0, 0, 0, 1).isEmpty)
      assert(!StatSheet(0, 0, 0, 1, 0).isEmpty)
      assert(!StatSheet(0, 0, 1, 0, 0).isEmpty)
      assert(!StatSheet(0, 1, 0, 0, 0).isEmpty)
      assert(!StatSheet(1, 0, 0, 0, 0).isEmpty)
    }
    "should be equal to an instance with the same values" in {
      assert(StatSheet() == StatSheet())
      assert(StatSheet(1, 2, 3, 4, 5) == StatSheet(1, 2, 3, 4, 5))
    }
    "should be equal to a copy of itself" in {
      assert(StatSheet() == StatSheet().copy())
      assert(StatSheet(1, 2, 3, 4, 5) == StatSheet(1, 2, 3, 4, 5).copy())
    }
    "should not be equal to a copy with one stat changed" in {
      assert(
        StatSheet(1, 2, 3, 4, 5) != StatSheet(1, 2, 3, 4, 5).copy(cpower = 0)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) != StatSheet(1, 2, 3, 4, 5).copy(cspeed = 0)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) != StatSheet(1, 2, 3, 4, 5).copy(health = 0)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) != StatSheet(1, 2, 3, 4, 5).copy(hregen = 0)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) != StatSheet(1, 2, 3, 4, 5).copy(movespeed = 0)
      )
    }
    "should be equal to a copy with one stat changed" in {
      assert(
        StatSheet(1, 2, 3, 4, 5) == StatSheet(1, 2, 3, 4, 5).copy(cpower = 1)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) == StatSheet(1, 2, 3, 4, 5).copy(cspeed = 2)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) == StatSheet(1, 2, 3, 4, 5).copy(health = 3)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) == StatSheet(1, 2, 3, 4, 5).copy(hregen = 4)
      )
      assert(
        StatSheet(1, 2, 3, 4, 5) == StatSheet(1, 2, 3, 4, 5).copy(movespeed = 5)
      )
    }
    "should be empty when subtracted from itself" in {
      StatSheet(1, 2, 3, 4, 5) - StatSheet(1, 2, 3, 4, 5) == StatSheet.empty
    }
    "should be scaled by a scalar" in {
      assert(StatSheet(1, 2, 3, 4, 5) * 2 == StatSheet(2, 4, 6, 8, 10))
      assert(StatSheet(1, 2, 3, 4, 5) / 2 == StatSheet(0.5f, 1, 1.5f, 2, 2.5f))
    }
    "should be added to another StatSheet" in {
      assert(
        StatSheet(1, 2, 3, 4, 5) + StatSheet(1, 2, 3, 4, 5) == StatSheet(2, 4,
          6, 8, 10)
      )
    }
    "should be subtracted from another StatSheet" in {
      assert(
        StatSheet(1, 2, 3, 4, 5) - StatSheet(1, 2, 3, 4, 5) == StatSheet.empty
      )
    }
  }
}
