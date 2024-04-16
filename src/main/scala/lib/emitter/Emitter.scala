package lib.emitter

// Todo: Make Emitter safe for Node destruction
final class Emitter[T] private[emitter] {
  private var listeners: List[T => Unit] = Nil

  def connect(f: T => Unit): Unit =
    listeners = f :: listeners

  def disconnect(f: T => Unit): Unit =
    require(listeners.contains(f), "Trying to disconnect non-existent lambda `f`.")
    listeners = listeners.filterNot(_ == f)

  def +=(f: T => Unit): Unit =
    this.connect(f)

  def -=(f: T => Unit): Unit =
    this.disconnect(f)

  private[emitter] def emit(data: T): Unit = listeners.foreach(_.apply(data))

  // This method can only be used when T is Unit. Doing otherwise will result in a compile-time error.
  private[emitter] def emit()(implicit ev: Unit =:= T): Unit =
    listeners.foreach(_.apply(()))
}
