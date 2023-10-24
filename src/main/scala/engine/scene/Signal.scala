package engine.scene

// Todo: Make Signals safe for Node destruction
private[scene] class Signal[D] {
  private var listeners: List[(D) => Unit] = Nil

  def +=(f: (D) => Unit): Unit =
    listeners = f :: listeners

  def -=(f: (D) => Unit): Unit =
    listeners = listeners.filterNot(_ == f)

  def emit(data: D): Unit = listeners.foreach(_.apply(data))
}
