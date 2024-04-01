package engine.core

// Todo: Make Signals safe for Node destruction
class Event[D] {
  private var listeners: List[(D) => Unit] = Nil

  def +=(f: (D) => Unit): Unit =
    listeners = f :: listeners

  def -=(f: (D) => Unit): Unit =
    listeners = listeners.filterNot(_ == f)

  def emit(data: D): Unit = listeners.foreach(_.apply(data))

  // This method can only be used when D is Unit. Doing otherwise will result in a compile-time error.
  def emit()(implicit ev: Unit =:= D): Unit = listeners.foreach(_.apply(()))
}
