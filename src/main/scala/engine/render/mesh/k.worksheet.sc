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
