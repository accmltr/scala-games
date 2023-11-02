import engine.math.nearEquals
import engine.test_utils.assertNotNearEquals
import engine.test_utils.assertNearEquals
import engine.math.Vector2
import engine.math.geometry.Polygon
import scala.util.boundary
import engine.math.given_Conversion_Float_ImplicitFloat

nearEquals(0.1f, 0.2f, 0.100000001f)
nearEquals(0.1f, 0.2f, 0.100000000f)
nearEquals(0.1f, 0.2f, 0.099999999f)
