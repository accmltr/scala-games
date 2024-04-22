package lib.event_emitter

final class Emitter[T] private[event_emitter] {
  private var _listeners: List[T => Unit] = Nil

  def connect(f: T => Unit): Unit =
    _listeners = _listeners :+ f

  def disconnect(f: T => Unit): Unit =
    require(_listeners.contains(f), "Trying to disconnect non-existent lambda `f`.")
    _listeners = _listeners.filterNot(_ == f)

  def +=(f: T => Unit): Unit =
    this.connect(f)

  def -=(f: T => Unit): Unit =
    this.disconnect(f)

  private[event_emitter] def emit(data: T): Unit = _listeners.foreach(_.apply(data))

  // This method can only be used when T is Unit. Doing otherwise will result in a compile-time error.
  private[event_emitter] def emit()(implicit ev: Unit =:= T): Unit =
    _listeners.foreach(_.apply(()))
}
