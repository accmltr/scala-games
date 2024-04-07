package general

import engine.core.World
import engine.core.Entity
import lib.instance_manager.Ref

class PizzaGuy private (using world: World) extends Entity {
  override def ref: Ref[PizzaGuy, Entity] =
    super.ref.asInstanceOf[Ref[PizzaGuy, Entity]]
}

object PizzaGuy {
  def apply(name: String)(using world: World): PizzaGuy = {
    val entity = new PizzaGuy()
    entity.name = name
    entity.makeReady()
    entity
  }
}
