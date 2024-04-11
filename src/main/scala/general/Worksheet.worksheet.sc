import engine.core.Entity
import engine.math.*

val T1 = Matrix3.translation(0, 1)
val T2 = Matrix3.rotation(pi)
T2.rotationValue
T2.scalingValue

val v = T1 * T2 * Vector2.zero
T2.inverse * T1.inverse * v
T1.inverse * T2.inverse * v

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

// Testing Vector2.angle

def anglesEqual(a1: Float, a2: Float, epsilon: Float = 0f): Boolean =
  angleInBounds(a1, a2 - epsilon, a2 + epsilon, false)

val a1 = Vector2(1, 0).angle
val a2 = -Vector2(-1, 0).angle
-a2 == pi
normalAngle(a2)
normalAngle(a1) == normalAngle(a2)
anglesEqual(a1, a2)
pi % -pi
-3.1 % 3.2
normalAngle(-pi)
Matrix3(rotation = (3f / 4f) * pi).rotationValue

Matrix3(rotation = -pi / 2f).rotationValue
Matrix3(rotation = pi / 2f).rotationValue
