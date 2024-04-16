import lib.emitter.OpenEmitter

val onHit = OpenEmitter[Unit]()

def f: Unit => Unit = _ => println("Hit!")
onHit += f

onHit.emit()

onHit -= f

onHit.emit()