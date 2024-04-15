// package engine.render.rendered_element

// import org.scalatest.flatspec.AnyFlatSpec
// import org.scalatest.matchers.should.Matchers
// import engine.test_utils.assertNearEquals
// import engine.math.Vector2

// class LineRenderedElementTest extends AnyFlatSpec with Matchers {

//   "default constructor" should "throw an exception when points is empty" in {
//     assertThrows[Exception] {
//       LineRenderedElement(Array.empty)
//     }
//   }

//   it should "throw an exception when points has only one element" in {
//     assertThrows[Exception] {
//       LineRenderedElement(Array(Vector2(0, 0)))
//     }
//   }

//   it should "throw an exception when width is 0 or negative" in {
//     assertThrows[Exception] {
//       LineRenderedElement(Array(Vector2(0, 0), Vector2(1, 1)), -1)
//     }
//     assertThrows[Exception] {
//       LineRenderedElement(Array(Vector2(0, 0), Vector2(1, 1)), 0)
//     }
//   }

//   it should "throw an exception when points has a 0 length segment" in {
//     assertThrows[Exception] {
//       LineRenderedElement(Array(Vector2(0, 0), Vector2(0, 0), Vector2(1, 1)))
//     }
//   }

//   "pointsToRectVerts" should "create rect" in {

//     val points1 = Array(Vector2(0, 0), Vector2(0, 1))

//     LineRenderedElement.pointsToRectVerts(points1, 2) shouldBe Array[Float](-1,
//       0, 1, 0, -1, 1, 1, 1)
//     LineRenderedElement.pointsToRectVerts(points1, 1) shouldBe Array[Float](
//       -0.5, 0, 0.5, 0, -0.5, 1, 0.5, 1)

//     val points2 = Array(Vector2(1, 0), Vector2(1, 2))

//     vertToVect(
//       LineRenderedElement.pointsToRectVerts(points2, 2)
//     ) shouldBe vertToVect(
//       Array[Float](0, 0, 2, 0, 0, 2, 2, 2)
//     )
//     vertToVect(
//       LineRenderedElement.pointsToRectVerts(points2, 1)
//     ) shouldBe vertToVect(
//       Array[Float](0.5, 0, 1.5, 0, 0.5, 2, 1.5, 2)
//     )
//   }

//   it should "create a rect for each line made by points" in {
//     val points1 =
//       Array(Vector2(0, 0), Vector2(1, 0), Vector2(1, 2))
//     val vertsExpected = vertToVect(
//       Array[Float](0, 1, 0, -1, 1, 1, 1, -1, 0, 0, 2, 0, 0, 2, 2, 2)
//     )
//     val vertsFound = vertToVect(
//       LineRenderedElement.pointsToRectVerts(points1, 2)
//     )

//     vertsFound.size shouldBe vertsExpected.size
//     vertsFound shouldBe vertsExpected
//   }

//   "lineIndices" should "create indices" in {
//     LineRenderedElement.lineIndices(2) shouldBe Array[Int](0, 1, 2, 2, 1, 3)
//     LineRenderedElement.lineIndices(3) shouldBe Array[Int](0, 1, 2, 2, 1, 3, 4,
//       5, 6, 6, 5, 7)
//   }

//   def vertToVect(verts: Array[Float]): Array[Vector2] = {
//     (for i <- 0 until verts.length / 2
//     yield Vector2(verts(i * 2), verts(i * 2 + 1))).toArray
//   }
// }
