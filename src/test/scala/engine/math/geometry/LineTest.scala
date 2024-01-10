package engine.math.geometry

import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals
import org.scalatest.flatspec.AnyFlatSpec
import engine.math.Vector2

class LineTest extends AnyFlatSpec with Matchers {

  "Line.intersection" should "return the intersection point of two lines" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 1), Vector2(1, 0))
    val istn = line1.intersection(line2)
    istn.match
      case None        => fail()
      case Some(value) => assertNearEquals(value, Vector2(0.5f, 0.5f))
  }

  it should "return the intersection point of two lines with negative coordinates" in {
    val line1 = Line(Vector2(-1, -1), Vector2(1, 1))
    val line2 = Line(Vector2(-1, 1), Vector2(1, -1))
    val istn = line1.intersection(line2)
    istn.match
      case None        => fail()
      case Some(value) => assertNearEquals(value, Vector2(0, 0))
  }

  it should "return the intersection of one endpoint on another line" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(-1, 0), Vector2(1, 0))
    val istn = line1.intersection(line2)
    istn.match
      case None        => fail()
      case Some(value) => assertNearEquals(value, Vector2(0, 0))
  }

  it should "return the intersection for two non-parallel endpoints" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 0))
    val line2 = Line(Vector2(0, 0), Vector2(0, 1))
    line1.intersection(line2) shouldBe Some(Vector2(0, 0))
    val line3 = Line(Vector2(1, 0), Vector2(1, 1))
    line1.intersection(line3) shouldBe Some(Vector2(1, 0))
  }

  it should "return None for two parallel endpoints on same position" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 0))
    val line2 = Line(Vector2(0, 0), Vector2(1, 0))
    line1.intersection(line2) shouldBe None
    val line3 = Line(Vector2(1, 0), Vector2(2, 0))
    line1.intersection(line3) shouldBe None
  }

  it should "return None for non-intersecting lines" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 1), Vector2(1, 2))
    assert(line1.intersection(line2).isEmpty)
  }

  it should "return None for parallel lines" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 0), Vector2(2, 2))
    assert(line1.intersection(line2).isEmpty)
  }

  "Line.contains(Vector2)" should "return true when the point is on the line" in {
    val line = Line(Vector2(0, 0), Vector2(1, 1))
    assert(line.contains(Vector2(0.5f, 0.5f)))
  }

  it should "return false when endpoints are excluded" in {
    val line = Line(Vector2(0, 0), Vector2(1, 1))
    assert(!line.contains(Vector2(0, 0), false))
    assert(!line.contains(Vector2(1, 1), false))
  }

  it should "return false for points not on the line" in {
    val line = Line(Vector2(0, 0), Vector2(1, 1))
    assert(!line.contains(Vector2(0, 1)))
    assert(!line.contains(Vector2(1, 0)))
    assert(!line.contains(Vector2(-10, -10)))
  }

  "Line.intersects" should "return true when crossing" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 1), Vector2(1, 0))
    assert(line1.intersects(line2, false))
  }

  it should "return false when not crossing" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 1), Vector2(1, 2))
    assert(!line1.intersects(line2))
    val line3 = Line(Vector2(-1, 2), Vector2(-1, 1))
    assert(!line1.intersects(line3))
  }

  it should "return false for parallel lines" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 0), Vector2(2, 2))
    assert(!line1.intersects(line2, false))
    val line3 = Line(Vector2(0, 0), Vector2(2, 2))
    assert(!line2.intersects(line3, true))
    val line4 = Line(Vector2(1, 1), Vector2(2, 2))
    assert(!line1.intersects(line4, true))
    assert(!line1.intersects(line4, false))
  }

  it should "return flase when parallel line endpoints are excluded" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(1, 1), Vector2(2, 2))
    assert(!line1.intersects(line2, false))
  }

  it should "return false when parallel line endpoints are included" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(1, 1), Vector2(2, 2))
    assert(!line1.intersects(line2, true))
  }

  it should "ignore endpoints when 'includeEndpoints' is false" in {
    val line1 = Line(Vector2(0.5, 0), Vector2(1, 1))
    val line2 = Line(Vector2(1, 1), Vector2(2, 2))
    assert(!line1.intersects(line2, false))
  }

  it should "include endpoints when 'includeEndpoints' is true" in {
    val line1 = Line(Vector2(0.5, 0), Vector2(1, 1))
    val line2 = Line(Vector2(1, 1), Vector2(2, 2))
    assert(line1.intersects(line2, true))
  }

  "Line.overlap" should "return true when crossing" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 1), Vector2(1, 0))
    assert(line1.overlaps(line2, false))
  }

  it should "ignore endpoints when 'includeEndpoints' is false" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(1, 1), Vector2(2, 2))
    assert(!line1.overlaps(line2, false))
    val line3 = Line(Vector2(0, 3), Vector2(1, 1))
    assert(!line1.overlaps(line3, false))
  }

  it should "include endpoints when 'includeEndpoints' is true" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(1, 1), Vector2(2, 2))
    assert(line1.overlaps(line2, true))
    val line3 = Line(Vector2(0, 3), Vector2(1, 1))
    assert(line1.overlaps(line3, true))
  }

}
