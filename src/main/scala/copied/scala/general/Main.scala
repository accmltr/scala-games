import engine.Game
import engine.input.KeyCode
import engine.math.Vector2
import engine.math.Vector2Implicits.given
import engine.input.MouseCode
import engine.rendering.window.{ScreenSize, FpsStats}
import engine.Node

@main def main: Unit =

  val root = Node(
    name = "MyFirstNode",
    position = (0, 0),
    scale = (1, 1),
    rotation = 0,
    children = List(
      Node("Child 1"),
      Node("Child 2"),
      Node("Child 3"),
      Node("Child 4")
    )
  )
  println(root)

  val game = Game(root)
  game.window.size = ScreenSize.p720
  game.window.maximized = false
  game.window.vsync = true
  game.window.fpsStats.showAvg = true
  // game.setScene(new scene.custom.EditorScene(game))
  game.run()
