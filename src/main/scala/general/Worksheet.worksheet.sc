import engine.math.Constants.pi
import engine.math._

1.00002f nearEquals 1.00001f

var square = Rectangle(10, 10)
var square_polygon = square.toPolygon
square.grow(2).toPolygon
square_polygon.grow(2)
var points = square_polygon.points
var isClockwise = square_polygon.isClockwise
var amount = 2f

val pts =
  List(
    Vector2(-5, 5),
    Vector2(5, 5),
    Vector2(5, -5),
    Vector2(-5, -5)
  )
val p1 = pts(0)
val p2 = pts(1)
val p3 = pts(2)
val p4 = pts(3)
isClockwise = pts(0) == Vector2(-5, 5)
isClockwise
val e1 = p1 - p2
val e2 = p2 - p3
val e3 = p3 - p4
val e4 = p4 - p1
val edge1: Vector2 = if isClockwise then p3 - p2 else p2 - p1
val edge2: Vector2 = if isClockwise then p2 - p1 else p3 - p2
Operations.toDegrees((p2 - p1).angleBetween(p3 - p2))
Operations.toDegrees((p2 - p3).angleBetween(p2 - p1))
Operations.toDegrees(edge1.angleBetween(edge2) / 2f)
val halfEdgeAngle: Float = edge1.angleBetween(edge2) / 2f
val l: Float = amount / Operations.sin(halfEdgeAngle)
Operations.toDegrees(edge1.angle)
Operations.toDegrees(edge2.angle)
if isClockwise
then Operations.toDegrees(edge1.angle + pi - halfEdgeAngle)
else Operations.toDegrees(edge2.angle + pi + halfEdgeAngle)
val a =
  if isClockwise
  then edge1.angle + pi - halfEdgeAngle
  else edge2.angle + pi + halfEdgeAngle
Operations.toDegrees(a)
var offset = Vector2.fromAngle(a, l)
val newPoint: Vector2 =
  p2 + offset
newPoint
