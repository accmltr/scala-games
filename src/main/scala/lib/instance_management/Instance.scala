package lib.instance_management

final class Instance[T] private[instance_management] (
    private var _instance: Option[T],
    val id: Int,
    val manager: InstanceManager[T]
) {
  require(
    _instance.isDefined,
    "'instance' parameter may not be null"
  )

  def isDestroyed: Boolean = _instance.isEmpty

  def destroy(): Unit = {
    _instance = None
    manager._onDestroyed(this)
  }
}

object Instance {
  // implicitly convert Reference[T] to T
  implicit def rti[T](ref: Instance[T]): T = {
    ref._instance match {
      case Some(value) => value
      case None =>
        throw new IllegalStateException(
          "Trying to access a destroyed Instance"
        )
    }
  }
}
