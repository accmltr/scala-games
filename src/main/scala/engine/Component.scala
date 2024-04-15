package engine

trait Component {
  def init(): Unit
  def physicsUpdate(delta: Float): Unit
  def renderUpdate(delta: Float): Unit
  def render(): Unit
}
