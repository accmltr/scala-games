package general

import engine.core.Entity
import engine.core.World
import lib.instance_manager.Ref

case class MyEntity protected ()(using
    world: World
) extends Entity {
  val kazam = "Kazam my man"
}

object MyEntity {
  def apply(name: String = "MyEntity")(using world: World): MyEntity =
    val e = new MyEntity
    e.name = name
    e.makeReady()
    e
}
