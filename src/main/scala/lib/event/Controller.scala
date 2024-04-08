package lib.event

final case class Controller[T] private () {
  val event: Event[T] = Event[T]()
  def emit(data: T): Unit = event.emit(data)
  def emit()(implicit ev: Unit =:= T): Unit = event.emit()
}

object Controller {
  def apply[T](): Controller[T] =
    new Controller[T]()
}
