import engine.math.nearEquals
import engine.test_utils.assertNotNearEquals
import engine.test_utils.assertNearEquals
import engine.math.Vector2
import engine.math.geometry.Polygon
import scala.util.boundary
import engine.math.given_Conversion_Float_ImplicitFloat

val a = 1.00001f
val b = 1.00000f
val epsilon = 0.00001f

a - b

a - b > epsilon
b - a < -epsilon

if ((a + b) < (2 * a)) {
  (a + b + epsilon) > (2 * a)
} else {
  (a + b) < ((2 * a) + epsilon)
}
