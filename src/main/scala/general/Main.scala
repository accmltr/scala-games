import engine.input.{KeyCode, MouseCode}
import engine.Game
import engine.input.KeyCode
import engine.math.Vector2
import engine.math.Vector2Implicits.given
import engine.input.MouseCode
import engine.rendering.window.{ScreenSize, FpsStats}
import engine.Node
import engine.Component

object MyGame extends Game {

  println("MyGame: Game Created")

  title = "MyGame"

  root = {
    val node = Node()
    node.name = "MyFirstNode"
    node.position = Vector2(0, 0)
    node.scale = Vector2(1, 1)
    node.rotation = 0
    node.children = List(
      Node(),
      Node(),
      Node(),
      Node()
    )
    node
  }

  println(root)

  window.size = ScreenSize.p720
  window.maximized = false
  window.vsync = true
  window.fpsStats.showAvg = true

  onInit += { (_) =>
    println("MyGame: Game Initialized")
  }
  println("Added onInit callback")

  onUpdate += { (delta: Float) =>
    // println(s"MyGame: Game Updated: $delta")
    // println(s"MyGame: Mouse Position: ${input.mousePosition}")
  }

  run()
}
