package lib.instance_management

final class Ref[T] private[instance_management] (
    val id: Int,
    val manager: InstanceManager[T]
) {
  def inst: Option[T] = manager.instance(id)
  def destroy(): Unit = manager.destroy(id)
}

// object Ref {
//   implicit def rti[T](ref: Ref[T]): T = ref.inst
// }
