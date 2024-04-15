import engine.core.Entity
import engine.math.*

val translation = Vector2(11f, 44f)
val rotation = pi / 2f
val scale = Vector2(2f, 3f)

val m = Matrix3(
  translation = translation,
  rotation = rotation,
  scale = scale
)

m.translation
m.rotation
m.scale
