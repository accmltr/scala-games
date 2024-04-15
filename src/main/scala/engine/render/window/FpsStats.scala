package engine.render.window

import scala.collection.immutable.Queue
import engine.Time
import engine.TimeImplicits.given

/** A class used to track FPS stats for a `Window` instance. */
private[render] class FpsStats(
    var showCurrent: Boolean = false,
    var showAvg: Boolean = false,
    var showMin: Boolean = false,
    var showMax: Boolean = false
) {

  // Private Fields
  var _current: Float = 0
  var _duration: Float = 1
  var _history: Queue[(Float, Float)] = Queue().enqueue((0f, 0f))
  var _avg: Float = 0
  var _min: Float = 0
  var _max: Float = 0

  // Setters
  def duration_=(duration: Float): Unit =
    if (duration > 0) _duration = duration

  // Public Methods
  private[window] def update(deltaTime: Float): Unit = {
    _current = (1 / deltaTime)
    _history = _history.enqueue((Time.toFloat, _current))
    while (Time - _history.head._1 > _duration)
      _history = _history.tail
    _avg = _history.map(_._2).sum / _history.size
    _min = _history.map(_._2).min
    _max = _history.map(_._2).max
  }

  // Getters
  /** The FPS as determined by the last frame. */
  def current: Float = _current

  /** The duration used to calculate the avg, min and max FPS. */
  def duration: Float = _duration

  /** The average FPS over the duration. */
  def avg: Float = _avg

  /** The minimum FPS over the duration. */
  def min: Float = _min

  /** The maximum FPS over the duration. */
  def max: Float = _max
}
