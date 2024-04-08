package lib.operation

import lib.event.Event
import lib.event.Controller

final case class Op() {
  private var _state = OpState.Idle

  private def _setState(value: OpState): Unit =
    assert(
      _state != value,
      "FOR DEVS OF Op CLASS ONLY: Trying to set state to its current value."
    )
    val previous = _state
    _state = value
    // onOpstateSet.emit((previous, value))

  def state = _state
  private val onStartController = Controller[Unit]()
  private val onCompleteController = Controller[Unit]()
  val onStart = onStartController.event
  val onComplete = onCompleteController.event

  /** This event contains the previous and new value for the `state` property.
    */
  // val onOpstateSet = Event[(OpState, OpState)]

  onStart.connect(_ => _setState(OpState.Running))
  onComplete.connect(_ => _setState(OpState.Idle))
  // SafeEvent
}

enum OpState {
  case Idle
  case Running
  // case Paused
}
