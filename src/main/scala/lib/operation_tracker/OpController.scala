package lib.operation_tracker

final case class OpController() {
  val op: Op = new Op

  def start(): Unit = op.start()

  def pause(): Unit = op.pause()

  def resume(): Unit = op.resume()

  def cancel(): Unit = op.cancel()

  def complete(): Unit = op.complete()
}
