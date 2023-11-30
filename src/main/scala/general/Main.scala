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

  title = "MyGame"

  root = {
    val node = Node()
    node.name = "MyFirstNode"
    node.position = (0, 0)
    node.scale = (1, 1)
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
  // setScene(new scene.custom.EditorScene(game))
  run()
}
