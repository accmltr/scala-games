package lib.emitter

/** Similar to `Emitter`, but has no controller and comes with public `emit` method. */
final class OpenEmitter[T] {
    private var listeners: List[T => Unit] = Nil

    def +=(f: T => Unit): Unit =
      listeners = f :: listeners

    def -=(f: T => Unit): Unit =
      listeners = listeners.filterNot(_ == f)

    def connect(f: T => Unit): Unit =
      this.+=(f)

    def disconnect(f: T => Unit): Unit =
      this.-=(f)

    def emit(data: T): Unit = listeners.foreach(_.apply(data))

    // This method can only be used when T is Unit. Doing otherwise will result in a compile-time error.
    def emit()(implicit ev: Unit =:= T): Unit =
      listeners.foreach(_.apply(()))

}
