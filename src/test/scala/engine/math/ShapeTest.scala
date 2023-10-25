package engine.math
import org.scalatest.freespec.AnyFreeSpec

class ShapeTest extends AnyFreeSpec {
  "Rectangle" - {
    "should contain its origin" in {
      val rectangle = Rectangle(10, 10)
      val point = Vector2(0, 0)
      assert(rectangle.contains(point))
    }
    "should contain a point inside" in {
      val rectangle = Rectangle(10, 10)
      val point = Vector2(1, 2)
      assert(rectangle.contains(point))
    }
    "should contain a point on the edge" in {
      val rectangle = Rectangle(10, 10)
      val point = Vector2(5, 5)
      assert(rectangle.contains(point))
    }
    "should not contain a point outside" in {
      val rectangle = Rectangle(10, 10)
      val point = Vector2(15, 15)
      assert(!rectangle.contains(point))
    }
    "should scale" in {
      val rectangle = Rectangle(10, 10)
      val scaled = rectangle.scale(2)
      assert(scaled.width == 20)
      assert(scaled.height == 20)
    }
    "should grow" in {
      val rectangle = Rectangle(10, 10)
      val grown = rectangle.grow(2)
      assert(grown.width == 14)
      assert(grown.height == 14)
    }
    "should convert to polygon" in {
      val rectangle = Rectangle(10, 10)
      val polygon = rectangle.toPolygon
      assert(polygon.points.length == 4)
      assert(polygon.points(0) == Vector2(-5, -5))
      assert(polygon.points(1) == Vector2(5, -5))
      assert(polygon.points(2) == Vector2(5, 5))
      assert(polygon.points(3) == Vector2(-5, 5))
    }
  }
  "Rectangle and Polygon" - {
    "should equal" in {
      val rectangle = Rectangle(10, 10)
      val polygon = Polygon(
        List(Vector2(-5, -5), Vector2(5, -5), Vector2(5, 5), Vector2(-5, 5))
      )
      assert(rectangle.toPolygon == polygon)
    }
    "should equal after grow" in {
      val rectangle = Rectangle(10, 10)
      val polygon = Polygon(
        List(Vector2(-5, -5), Vector2(5, -5), Vector2(5, 5), Vector2(-5, 5))
      )
      val grownRectangle = rectangle.grow(2)
      val grownPolygon = polygon.grow(2)
      assert(grownRectangle.toPolygon == grownPolygon)
    }
  }
}
