package general

import engine.core.Entity
import engine.core.World
import lib.instance_management.Ref

final case class MyEntity()(using
    world: World
) extends Entity {
  val kazam = "Kazam my man"
}

// object MyEntity {
//   def apply(name: String)(using world: World): MyEntity =
//     val e = new MyEntity
//     e.name = name
//     e
// }
