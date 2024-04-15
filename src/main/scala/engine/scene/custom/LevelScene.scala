package engine.scene.custom

import engine.scene.Scene
import engine.core.World
import engine.input.KeyCode
import engine.render.window.Resolution

import org.lwjgl.stb.STBImage._
import engine.input.KeyCode

class LevelScene(world: World) extends Scene(world) {

  var imagePath = "src/resources/sample_image.png"

  override def init(): Unit = {
    world.window.title = "Level Scene"
  }

  override def renderUpdate(delta: Float): Unit = {
    if (world.input.justPressed(KeyCode.w))
      println("I am in the level scene")

  }

  override def physicsUpdate(delta: Float): Unit = {}

}
