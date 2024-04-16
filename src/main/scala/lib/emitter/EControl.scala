package lib.emitter

/** This class allows one to emit events on its `Emitter`. Keeping this class
  * private ensures that emits are not triggered from unexpected places.
  */
final case class EControl[T] private () {
  val emitter: Emitter[T] = new Emitter[T]()
  def emit(data: T): Unit = emitter.emit(data)
  def emit()(implicit data: Unit =:= T): Unit = emitter.emit()
}

object EControl {
  def apply[T](): EControl[T] =
    new EControl[T]()
}
