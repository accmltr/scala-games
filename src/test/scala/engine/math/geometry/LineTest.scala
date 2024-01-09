package engine.math.geometry

import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals
import org.scalatest.flatspec.AnyFlatSpec
import engine.math.Vector2

class LineTest extends AnyFlatSpec with Matchers {

  "Line.contains(Vector2)" should "return true when the point is on the line" in {
    val line = Line(Vector2(0, 0), Vector2(1, 1))
    assert(line.contains(Vector2(0.5f, 0.5f)))
  }

  it should "return false when endpoints are excluded" in {
    val line = Line(Vector2(0, 0), Vector2(1, 1))
    assert(!line.contains(Vector2(0, 0), false))
    assert(!line.contains(Vector2(1, 1), false))
  }

  "Line.intersects" should "return true when crossing" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 1), Vector2(1, 0))
    assert(line1.intersects(line2, false))
  }

  it should "return false for parallel lines" in {
    val line1 = Line(Vector2(0, 0), Vector2(1, 1))
    val line2 = Line(Vector2(0, 0), Vector2(2, 2))
    val line3 = Line(Vector2(0, 0), Vector2(2, 2))
    assert(!line1.intersects(line2, false))
    assert(!line2.intersects(line3, true))
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

  }

}
