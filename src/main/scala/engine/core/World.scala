package engine.core

import engine.input.Input
import engine.math.Matrix3
import engine.render.renderer.DefaultRenderer
import engine.render.window.Window
import lib.emitter.*
import lib.instance_manager.{InstanceManager, Ref}
import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.opengl.*
import org.lwjgl.system.*

abstract class World extends App {

  // Givens
  given World = this

  private val onEntityReadyController = EmitterController[Entity]()
  val onEntityReady = onEntityReadyController.emitter
  private val onEntityDestroyQueuedController = EmitterController[Entity]()
  val onEntityDestroyQueued = onEntityDestroyQueuedController.emitter
  private val onEntityDestroyedController = EmitterController[Entity]()
  val onEntityDestroyed = onEntityDestroyedController.emitter

  // Instance Management
  private[core] val _entityManager = InstanceManager[Entity]()
  private var _title: String = "Scala Games: Untitled Game"
  private var _initialized: Boolean = false
  private var _root: Entity = null
  private val _input: Input = Input()
  private val _window: Window =
    Window(_title, input.mouseListener, input.keyListener)
  private val _renderer = DefaultRenderer(window)
  private var _rootEntities: List[Entity] = Nil

  // Connect to entity manager
  _entityManager.onRegister.connect(_onEntityManagerRegister)
  _entityManager.onDestroying.connect(_onEntityManagerDestroying)

  private def _onEntityManagerRegister(ref: Ref[Entity, Entity]): Unit =
    ref.map(e =>
      println(s"Registered: $e")
      // Add to root entities if parent null.
      e.parent match
        case None =>
          _rootEntities = _rootEntities :+ e
          print(s"${e.name} added to root entities.")
        case _ =>

      // Connect to Entity life-cycle events for broadcasting.
      e.onReady.connect(_ => onEntityReadyController.emit(e))
      e.onDestroyQueued.connect(_ => onEntityDestroyQueuedController.emit(e))
      e.onDestroyed.connect(_ => onEntityDestroyedController.emit(e))
    )

  private def _onEntityManagerDestroying(ref: Ref[Entity, Entity]): Unit =
    ref.map(e =>
      if (_rootEntities.contains(e))
        _rootEntities = _rootEntities.filterNot(_ == e)
        print(s"${e.name} removed from root entities.")
    )

  // Public Methods
  def run(): Unit = {
    assert(!_initialized, "Game has already been run.")
    _initialized = true
    window.run()
  }

  def quit(): Unit = window.close()

  // def root: Entity = _root
  // def root_=(node: Entity): Unit = {
  //   if (node == null)
  //     throw new IllegalArgumentException("The root node may not be null")
  //   if (node.parent.isDefined)
  //     throw new IllegalArgumentException("The root node may not have a parent")
  //   _root = node
  // }

  // Temp Callback Exposure
  private val onInitController = EmitterController[Unit]()
  val onInit = onInitController.emitter
  private val onUpdateController = EmitterController[Float]()
  val onUpdate = onUpdateController.emitter

  // Handle window events
  _window.onInit.connect(_ => onInitController.emit(()))
  _window.onRender.connect(delta =>
    onUpdateController.emit(delta)

    // Render all render elements set on the entities in the order of the entity hierarchy
    _renderer.render(
      entities
        .flatMap(e =>
          e.renderElements
            .map(re =>
              val rd = re.renderData
              rd.copy(
                transform = e.globalTransform * rd.transform
              )
            )
        )
    )
  )

  private[core] def destroy(entity: Entity): Unit = {
    // _entityManager.destroy(entity
  }

  // Getters
  def title: String = _title
  def initialized: Boolean = _initialized
  def input: Input = _input
  def window: Window = _window
  def rootEntities: List[Entity] = _rootEntities
  def entities: List[Entity] =
    rootEntities.flatMap(e => e :: e.descendants)

  // Setters
  def title_=(value: String): Unit = {
    _window.title = value
    _title = value
  }
}
