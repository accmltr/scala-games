import engine.math.geometry.Line
import engine.math.shapes.Polygon
import engine.math.*
import engine.math.shapes.Circle
import engine.render.mesh.Mesh

val l1 = Line(Vector2(-1, -1), Vector2(1, 1))
val l2 = Line(Vector2(0, 0), Vector2(1, 0))

l1 intersects (l2, true)

l2 contains (Vector2(0, 0), false)
l1 contains Vector2(0.5, 0.5)

val l3 = Line(Vector2(0, 0), Vector2(1, 1))

l1 intersects l3

(0 until 4)
  .map(index => (index, if index + 1 == 4 then 0 else index + 1))
  .toList

(1, 3, 2, 5).toList

def linesEq(l1: (Int, Int), l2: (Int, Int)): Boolean = {
  l1 == l2 || l1 == (l2._2, l2._1)
}

linesEq((0, 1), (1, 0))
linesEq((0, 1), (0, 1))

def isLineInTri(l: (Int, Int), t: (Int, Int, Int)): Boolean = {
  linesEq(l, (t._1, t._2)) ||
  linesEq(l, (t._2, t._3)) ||
  linesEq(l, (t._3, t._1))
}

isLineInTri((1, 0), (4, 0, 1))
isLineInTri((0, 1), (4, 0, 1))
isLineInTri((4, 1), (4, 0, 1))
isLineInTri((4, 0), (4, 0, 1))
isLineInTri((0, 4), (4, 0, 1))

def trisEq(t1: (Int, Int, Int), t2: (Int, Int, Int)): Boolean = {
  isLineInTri((t1._1, t1._2), t2) &&
  isLineInTri((t1._2, t1._3), t2) &&
  isLineInTri((t1._3, t1._1), t2)
}

trisEq((3, 0, 1), (3, 0, 1))
trisEq((3, 0, 1), (0, 3, 1))
trisEq((3, 0, 1), (1, 3, 0))

// Square polygon
val polygon = Polygon(
  Vector(
    Vector2(0, 0),
    Vector2(1, 0),
    Vector2(1, 1),
    Vector2(0, 1)
  )
)

polygon.contains(Vector2(0.5, 0.5))
polygon.contains(Vector2(0, 0))

polygon.contains(Line(Vector2(0.1, 0.1), Vector2(0.5, 0.5)))
polygon.contains(Line(Vector2(0.1, 0), Vector2(0.5, 0.5)))
polygon.contains(Line(Vector2(0, 0), Vector2(0.5, 0.5)))
