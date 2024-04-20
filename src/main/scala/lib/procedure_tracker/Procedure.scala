package lib.procedure_tracker

import lib.event_emitter.*
import lib.procedure_tracker.State.*
import lib.property_tracker.{Property, PropertyController}

final case class Procedure private[procedure_tracker]() {
  private val _statePropertyController = PropertyController(Idle)
  val state: Property[State] = _statePropertyController.property

  private val _onStartedController = EmitterController[Unit]()
  private val _onPausedController = EmitterController[Unit]()
  private val _onResumedController = EmitterController[Unit]()
  private val _onCancelledController = EmitterController[Unit]()
  private val _onCompletedController = EmitterController[Unit]()
  private val _onRanController = EmitterController[Unit]()
  private val _onFinishedController = EmitterController[Unit]()
  /** Emitted when either 'start' called. */
  val onStarted: Emitter[Unit] = _onStartedController.emitter
  /** Emitted when either 'pause' called. */
  val onPaused: Emitter[Unit] = _onPausedController.emitter
  /** Emitted when either 'resume' called. */
  val onResumed: Emitter[Unit] = _onResumedController.emitter
  /** Emitted when either 'cancel' called. */
  val onCancelled: Emitter[Unit] = _onCancelledController.emitter
  /** Emitted when either 'complete' called. */
  val onCompleted: Emitter[Unit] = _onCompletedController.emitter
  /** Emitted when either 'start' or 'resume' called. */
  val onRan: Emitter[Unit] = _onRanController.emitter
  /** Emitted when either 'completed' or 'cancelled' called. */
  val onFinished: Emitter[Unit] = _onFinishedController.emitter

  private[procedure_tracker] def start(): Unit =
    require(state.value == Idle, "Can only 'start' on idle procedure. [Tip: Use 'resume' or 'cancel' to leave paused state]")
    _statePropertyController.value = Running
    _onStartedController.emit()
    _onRanController.emit()

  private[procedure_tracker] def pause(): Unit =
    require(state.value == Running, "Can only 'pause' on running procedure.")
    _statePropertyController.value = Paused
    _onPausedController.emit()

  private[procedure_tracker] def resume(): Unit =
    require(state.value == Paused, "Can only 'resume' on paused procedure.")
    _statePropertyController.value = Running
    _onResumedController.emit()
    _onRanController.emit()

  private[procedure_tracker] def cancel(): Unit =
    require(state.value == Running || state.value == Paused, "Can only 'cancel' on running or paused procedure.")
    _statePropertyController.value = Idle
    _onCancelledController.emit()
    _onFinishedController.emit()

  private[procedure_tracker] def complete(): Unit =
    require(state.value == Running, "Can only 'complete' on running procedure. [Tip: Use 'resume' when paused before using 'complete']")
    _statePropertyController.value = Idle
    _onCompletedController.emit()
    _onFinishedController.emit()
}


