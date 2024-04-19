package lib.procedure_tracker

final case class ProcedureController() {
  val op: Procedure = new Procedure

  def start(): Unit = op.start()

  def pause(): Unit = op.pause()

  def resume(): Unit = op.resume()

  def cancel(): Unit = op.cancel()

  def complete(): Unit = op.complete()
}
