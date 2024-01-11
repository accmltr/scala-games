package engine.render.rendered_element

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals
import engine.render.rendered_element.LineRenderedElement
import engine.math.Vector2

class LineRenderedElement extends AnyFlatSpec with Matchers {
  "pointsToRectVerts" should "create rectangle" in {

    val points1 = Array(Vector2(0, 0), Vector2(0, 1))

    Line.pointsToRectVerts(points1, 2) shouldBe Array[Float](-1, 0, 1, 0, 1, 1,
      -1, 1)
    Line.pointsToRectVerts(points1, 1) shouldBe Array[Float](-0.5, 0, 0.5, 0,
      0.5, 1, -0.5, 1)

    val points2 = Array(Vector2(1, 0), Vector2(1, 2))

    Line.pointsToRectVerts(points2, 2) shouldBe Array[Float](-0, 0, 2, 0, 1, 2,
      -0, 1)
  }
}
