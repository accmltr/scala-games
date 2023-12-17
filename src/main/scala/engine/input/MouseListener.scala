package engine.input

import org.lwjgl.glfw.GLFW._
import engine.math.Vector2
import engine.math.Vector2Implicits.given

private[engine] class MouseListener {

  private var _scroll: Vector2 = Vector2.zero
  private var _position: Vector2 = Vector2.zero
  private var _lastPosition: Vector2 = Vector2.zero
  private var _buttonsPressed: Set[Int] = Set.empty
  private var _buttonsJustPressed: Set[Int] = Set.empty
  private var _buttonsJustReleased: Set[Int] = Set.empty
  private var _isDragging: Boolean = false

  def endFrame(): Unit = {
    _scroll = Vector2.zero
    _buttonsJustPressed = Set.empty
    _buttonsJustReleased = Set.empty
  }

  def cursorPositionCallback(GLFWwindow: Long, x: Double, y: Double): Unit = {
    _lastPosition = _position
    _position = Vector2(x, y)
    _isDragging = _buttonsPressed.contains(GLFW_MOUSE_BUTTON_LEFT)
  }

  def mouseButtonCallback(
      GLFWwindow: Long,
      button: Int,
      action: Int,
      mods: Int
  ): Unit = {
    if (action == GLFW_PRESS) {
      assert(!_buttonsPressed.contains(button))
      _buttonsPressed += button
      _buttonsJustPressed += button
    } else if (action == GLFW_RELEASE) {
      _buttonsPressed -= button
      _buttonsJustReleased += button
      if (button == GLFW_MOUSE_BUTTON_LEFT) _isDragging = false
    }
  }

  def scrollCallback(GLFWwindow: Long, deltaX: Double, deltaY: Double): Unit =
    _scroll = Vector2(deltaX, deltaY)

  // Public Accessors
  def scroll: Vector2 = _scroll
  def position: Vector2 = _position
  def delta: Vector2 = position - _lastPosition

  def buttonsPressed: Set[MouseCode] = _buttonsPressed.map(MouseCode.from(_))
  def buttonPressed(mouseCode: MouseCode): Boolean =
    val code = mouseCode.code
    assert(code >= 0 && code < 8, "Invalid mouse button index")
    _buttonsPressed.contains(code)

  def buttonsJustPressed: Set[MouseCode] =
    _buttonsJustPressed.map(MouseCode.from(_))
  def buttonJustPressed(mouseCode: MouseCode): Boolean =
    val code = mouseCode.code
    assert(code >= 0 && code < 8, "Invalid mouse button index")
    _buttonsJustPressed.contains(code)

  def buttonsJustReleased: Set[MouseCode] =
    _buttonsJustReleased.map(MouseCode.from(_))
  def buttonJustReleased(mouseCode: MouseCode): Boolean =
    val code = mouseCode.code
    assert(code >= 0 && code < 8, "Invalid mouse button index")
    _buttonsJustReleased.contains(code)

  def isDragging: Boolean = _isDragging

}
