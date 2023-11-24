package lib.instance_management

final class Instance[T] private[instance_management] (
    private var instance: Option[T],
    val id: Int,
    val manager: InstanceManager[T]
) {
  require(
    instance.isDefined,
    "'instance' parameter may not be null"
  )

  def isDestroyed: Boolean = instance.isEmpty

  def destroy(): Unit = {
    instance = None
    manager.onDestroyed(this)
  }
}

object Instance {
  // implicitly convert Reference[T] to T
  implicit def rti[T](ref: Instance[T]): T = {
    ref.instance match {
      case Some(value) => value
      case None =>
        throw new IllegalStateException(
          "Trying to access a destroyed Instance"
        )
    }
  }
}
