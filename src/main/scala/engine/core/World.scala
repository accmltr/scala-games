package engine.core

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
import engine.render.window.Window
import engine.input.Input
import engine.input.{KeyListener, MouseListener, Input}
import lib.instance_management.InstanceManager
import lib.instance_management.Instance
import engine.scene.Signal

abstract class World extends App {

  // Givens
  given World = this

  private val instanceManager: InstanceManager[Node] =
    InstanceManager()

  private var _title: String = "Scala Games: Untitled Game"
  private var _initialized: Boolean = false
  private var _root: Node = null
  private val _input: Input = Input()
  private val _window: Window = Window(
    _title,
    input.mouseListener,
    input.keyListener,
    initCallback,
    updateCallback
  )

  // Node Management
  def register(node: Node): Instance[Node] = {
    instanceManager.register(node)
  }

  // Public Methods
  def run(): Unit = {
    assert(!_initialized, "Game has already been initialized")
    _initialized = true
    window.run()
  }

  def quit(): Unit = {
    glfwSetWindowShouldClose(window.windowId, true)
  }

  def root: Node = _root

  def root_=(node: Node): Unit = {
    if (node == null)
      throw new IllegalArgumentException("The root node may not be null")
    if (node.parent.isDefined)
      throw new IllegalArgumentException("The root node may not have a parent")
    _root = node
  }

  // Temp Callback Exposure
  val onInit: Signal[Unit] = Signal()
  val onUpdate: Signal[Float] = Signal()

  // Engine-side Methods
  private[engine] def initCallback(): Unit = {
    onInit.emit(())
  }
  private[engine] def updateCallback(delta: Float): Unit = {
    onUpdate.emit(delta)
  }

  // Getters
  def title: String = _title
  def initialized: Boolean = _initialized
  def input: Input = _input
  def window: Window = _window

  // Setters
  def title_=(value: String): Unit = {
    _window.title = value
    _title = value
  }
}
