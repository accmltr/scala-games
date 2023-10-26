import engine.math.Constants.pi
import engine.math._

var square = Rectangle(10, 10)
var square_polygon = square.toPolygon
var points = square_polygon.points
var isClockwise = square_polygon.isClockwise
var amount = 2f

val p1 = Vector2(-5, 5)
val p2 = Vector2(5, 5)
val p3 = Vector2(5, -5)
val p4 = Vector2(-5, -5)
val e1 = p2 - p1
val e2 = p3 - p2
val e3 = p4 - p3
val e4 = p1 - p4
val halfExternalAngle = e1.angleBetween(e2) / 2f
val l = amount / Operations.sin(halfExternalAngle)
val a = e1.angle + halfExternalAngle
val offset = Vector2.fromAngle(a, l)
val newPoint = p2 + offset

val a2 = e2.angle + halfExternalAngle
val offset2 = Vector2.fromAngle(a2, l)
val a3 = e3.angle + halfExternalAngle
val offset3 = Vector2.fromAngle(a3, l)
val a4 = e4.angle + halfExternalAngle
val offset4 = Vector2.fromAngle(a4, l)

def _aux(
    remaining: List[Vector2],
    previous: Vector2 = points.lastOption.getOrElse(Vector2.zero)
): List[Vector2] = {
  remaining match {
    case Nil => Nil
    case head :: next =>
      val nextPoint = next.headOption.getOrElse(points.head)
      if isClockwise then {
        val edge1: Vector2 = head - previous
        val edge2: Vector2 = nextPoint - head
        val halfEdgeAngle: Float = edge1.angleBetween(edge2) / 2f
        val l: Float = amount / Operations.sin(halfEdgeAngle)
        val a = edge1.angle + halfEdgeAngle
        val newPoint: Vector2 =
          head + Vector2.fromAngle(a, l)
        newPoint :: _aux(next, head)
      } else {
        val edge1: Vector2 = nextPoint - head
        val edge2: Vector2 = head - previous
        val halfEdgeAngle: Float = edge1.angleBetween(edge2) / 2f
        val l: Float = amount / Operations.sin(halfEdgeAngle)
        val a = edge1.angle + halfEdgeAngle
        val newPoint: Vector2 =
          head + Vector2.fromAngle(a, l)
        newPoint :: _aux(next, head)
      }
  }
}

val polygon = Polygon(_aux(points))
