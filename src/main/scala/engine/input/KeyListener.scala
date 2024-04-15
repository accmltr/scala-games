package engine.input

import org.lwjgl.glfw.GLFW._

private[engine] class KeyListener {

  private var _keysPressed: Set[Int] = Set.empty
  private var _keysJustPressed: Set[Int] = Set.empty
  private var _keysJustReleased: Set[Int] = Set.empty

  def endFrame(): Unit = {
    _keysJustPressed = Set.empty
    _keysJustReleased = Set.empty
  }

  def keyCallback(
      GLFWwindow: Long,
      key: Int,
      scancode: Int,
      action: Int,
      mods: Int
  ): Unit = {
    if (action == GLFW_PRESS) {
      assert(!_keysPressed.contains(key))
      _keysPressed += key
      _keysJustPressed += key
    } else if (action == GLFW_RELEASE) {
      _keysPressed -= key
      _keysJustReleased += key
    }
  }

  // Public Accessors
  def keysPressed: Set[KeyCode] = _keysPressed.map(KeyCode.from(_))
  def keyPressed(keyCode: KeyCode): Boolean =
    val code = keyCode.code
    assert(code >= 0 && code < 350, "Invalid key index")
    _keysPressed.contains(code)

  def keysJustPressed: Set[KeyCode] = _keysJustPressed.map(KeyCode.from(_))
  def keyJustPressed(keyCode: KeyCode): Boolean =
    val code = keyCode.code
    assert(code >= 0 && code < 350, "Invalid key index")
    _keysJustPressed.contains(code)

  def keysJustReleased: Set[KeyCode] = _keysJustReleased.map(KeyCode.from(_))
  def keyJustReleased(keyCode: KeyCode): Boolean =
    val code = keyCode.code
    assert(code >= 0 && code < 350, "Invalid key index")
    _keysJustReleased.contains(code)

}
