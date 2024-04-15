package engine.scene

trait EngineListener {
  def init(): Unit
  def renderUpdate(delta: Float): Unit
  def physicsUpdate(delta: Float): Unit
}
