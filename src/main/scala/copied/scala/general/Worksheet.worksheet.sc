import engine.math.Vector2
import engine.math.Vector2Implicits.given

var pos: Vector2 = (3, 3) + (3, 1) + Vector2.down

print(pos.formatted(2))

var s: String = pos.formatted(0)
