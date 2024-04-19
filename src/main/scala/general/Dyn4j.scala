package general

import engine.core.{Entity, World}
import lib.instance_manager.Ref

class Dyn4j private(using world: World) extends Entity {
  override def ref: Ref[Dyn4j, Entity] =
    super.ref.asInstanceOf[Ref[Dyn4j, Entity]]

  world.onInit += (_ =>
    println("Dyn4j instance initialized")
    )

  world.onUpdate += (_ =>
    println("Dyn4j instance update")
    )
}

object Dyn4j {
  def apply(name: String)(using world: World): Dyn4j = {
    val entity = new Dyn4j()
    entity.name = name
    entity.makeReady()
    entity
  }
}