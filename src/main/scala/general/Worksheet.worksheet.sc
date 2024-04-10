import engine.core.Entity
import engine.math.*

val T1 = Matrix3.translation(0, 1)
val T2 = Matrix3.rotation(pi)
T2.rotationValue
T2.scalingValue

val v = T1 * T2 * Vector2.zero
T2.inverse * T1.inverse * v
T1.inverse * T2.inverse * v

/*


















 */

var localTransform = Matrix3()
var origin = Vector2.zero

localTransform * origin
localTransform * Vector2.one

localTransform = Matrix3(
  Vector2(0, 10),
  pi,
  Vector2.one
)

val v2 = localTransform * origin
localTransform.inverse * v2
