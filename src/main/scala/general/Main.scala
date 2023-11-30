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

  title = "Scala Games: My First Game"

  root = Node()
  root.name = "MyFirstNode"
  root.position = (0, 0)
  root.scale = (1, 1)
  root.rotation = 0
  root.children = List(
    Node(),
    Node(),
    Node(),
    Node()
  )

  println(root)

  window.size = ScreenSize.p720
  window.maximized = false
  window.vsync = true
  window.fpsStats.showAvg = true
  // setScene(new scene.custom.EditorScene(game))
  run()
}
