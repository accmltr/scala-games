package general

import engine.core.{Entity, World}
import lib.instance_manager.Ref

class Wolf private (using world: World) extends Entity {
  override def ref: Ref[Wolf, Entity] =
    super.ref.asInstanceOf[Ref[Wolf, Entity]]
}

object Wolf {
  def apply(name: String)(using world: World): Wolf = {
    val entity = new Wolf()
    entity.name = name
    entity.makeReady()
    entity
  }
}
