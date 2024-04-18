package lib.operation_tracker

import lib.event_emitter.*
import lib.operation_tracker.OpState.*
import lib.property_tracker.{Property, PropertyController}

final case class Op private[operation_tracker]() {
  private val _statePropertyController = PropertyController(Idle)
  val state: Property[OpState] = _statePropertyController.property

  private val _onStartedController = EmitterController[Unit]()
  private val _onPausedController = EmitterController[Unit]()
  private val _onResumedController = EmitterController[Unit]()
  private val _onCancelledController = EmitterController[Unit]()
  private val _onCompletedController = EmitterController[Unit]()
  val onStarted: Emitter[Unit] = _onStartedController.emitter
  val onPaused: Emitter[Unit] = _onPausedController.emitter
  val onResumed: Emitter[Unit] = _onResumedController.emitter
  val onCancelled: Emitter[Unit] = _onCancelledController.emitter
  val onCompleted: Emitter[Unit] = _onCompletedController.emitter

  private[operation_tracker] def start(): Unit =
    require(state.value == Idle, "Cannot start non-idle operation. [Tip: Use 'resume' to continue operation when paused]")
    _statePropertyController.value = Running
    _onStartedController.emit()

  private[operation_tracker] def pause(): Unit =
    require(state.value == Running, "Cannot pause non-running operation.")
    _statePropertyController.value = Paused
    _onPausedController.emit()

  private[operation_tracker] def resume(): Unit =
    require(state.value == Paused, "Cannot resume non-paused operation.")
    _statePropertyController.value = Running
    _onResumedController.emit()

  private[operation_tracker] def cancel(): Unit =
    require(state.value == Running || state.value == Paused, "Cannot cancel idle operation.")
    _statePropertyController.value = Idle
    _onCancelledController.emit()

  private[operation_tracker] def complete(): Unit =
    require(state.value == Running, "Cannot complete non-running operation. [Tip: Resume if paused in order to complete]")
    _statePropertyController.value = Idle
    _onCompletedController.emit()
}

enum OpState {
  case Idle
  case Running
  case Paused
}
