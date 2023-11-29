package engine

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.system._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._
import engine.Time
import engine.TimeImplicits.given
import engine.input.{MouseListener, KeyListener}
import engine.math.Vector2
import engine.rendering.window.Window
import engine.input.Input
import engine.Node
import engine.input.{KeyListener, MouseListener, Input}

class Game(title: String = "Scala Games: Untitled Game") {

  private[this] var _initialized: Boolean = false
  private[this] var _root: Node = null
  private[this] val _input: Input = Input()
  private[this] val _window: Window = Window(
    title,
    input.mouseListener,
    input.keyListener,
    initCallback,
    updateCallback
  )

  // Public Methods
  def run(): Unit = {
    assert(!_initialized, "Game has already been initialized")
    _initialized = true
    window.run()
  }
  def root: Node = _root
  def root_=(node: Node): Unit = {
    if (node == null)
      throw new IllegalArgumentException("The root node may not be null")
    if (node.parent.isDefined)
      throw new IllegalArgumentException("The root node may not have a parent")
    _root = node
  }

  // Engine-side Methods
  private[engine] def initCallback(): Unit = {}
  private[engine] def updateCallback(delta: Float): Unit = {}

  // Getters
  def initialized: Boolean = _initialized
  def input: Input = _input
  def window: Window = _window
}
