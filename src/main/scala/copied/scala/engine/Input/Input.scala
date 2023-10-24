package engine.input

import engine.math.Vector2
import engine.math.Vector2Implicits.given

class Input {

  private[engine] val keyListener = new KeyListener()
  private[engine] val mouseListener = new MouseListener()

  def mousePosition: Vector2 = mouseListener.position
  def mouseDelta: Vector2 = mouseListener.delta
  def mouseScroll: Vector2 = mouseListener.scroll

  def pressed(pressable: Pressable): Boolean =
    pressable match
      case key: KeyCode     => keyListener.keyPressed(key)
      case mouse: MouseCode => mouseListener.buttonPressed(mouse)
      case _                => false

  def justPressed(pressable: Pressable): Boolean =
    pressable match
      case key: KeyCode     => keyListener.keyJustPressed(key)
      case mouse: MouseCode => mouseListener.buttonJustPressed(mouse)
      case _                => false

  def justReleased(pressable: Pressable): Boolean =
    pressable match
      case key: KeyCode     => keyListener.keyJustReleased(key)
      case mouse: MouseCode => mouseListener.buttonJustReleased(mouse)
      case _                => false

}
