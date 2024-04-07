package general

import engine.core.Entity
import engine.core.World
import lib.instance_manager.Ref

// case class MyEntity protected ()(using
//     world: World
// ) extends Entity {
//   val kazam = "Kazam my man"
// }

// object MyEntity {
//   def apply(name: String = "MyEntity")(using world: World): MyEntity =
//     val e = new MyEntity
//     e.name = name
//     e.makeReady()
//     e
// }

class Player private[general] (using world: World) extends Entity {
  override def ref: Ref[Player, Entity] =
    super.ref.asInstanceOf[Ref[Player, Entity]]
}

class SpecPlayer private (using world: World) extends Player {
  override def ref: Ref[SpecPlayer, Entity] =
    super.ref.asInstanceOf[Ref[SpecPlayer, Entity]]
}

object Player {
  def apply(name: String)(using world: World): Player =
    val p = new Player
    p.name = name
    p.makeReady()
    p
}

object SpecPlayer {
  def apply(name: String)(using world: World): Player =
    val p = new SpecPlayer
    p.name = name
    p.makeReady()
    p
}
