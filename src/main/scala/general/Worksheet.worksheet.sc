import engine.math.Vector2
import engine.math.geometry.Polygon
import scala.util.boundary

def f =
  boundary:
    for (i <- 1 to 10)
    do
      if i % 2 == 0 then boundary.break(i)
      else println(i)

f

var polygon1 = Polygon(
  Vector(Vector2(-5, 5), Vector2(5, 5), Vector2(5, -5), Vector2(-5, -5))
)
var polygon2 = Polygon(
  Vector(
    Vector2(-5.11, 5),
    Vector2(5, 5),
    Vector2(5, -5),
    Vector2(-5, -5)
  )
)
polygon1 nearEquals (polygon2, .11f)
