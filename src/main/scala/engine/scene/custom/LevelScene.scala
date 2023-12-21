package engine.scene.custom

import engine.scene.Scene
import engine.Game
import engine.input.KeyCode
import engine.render.window.ScreenSize

import org.lwjgl.stb.STBImage._
import engine.input.KeyCode

class LevelScene(game: Game) extends Scene(game) {

  var imagePath = "src/resources/sample_image.png"

  override def init(): Unit = {
    game.window.title = "Level Scene"
  }

  override def renderUpdate(delta: Float): Unit = {
    if (game.input.justPressed(KeyCode.w))
      println("I am in the level scene")

  }

  override def physicsUpdate(delta: Float): Unit = {}

}
