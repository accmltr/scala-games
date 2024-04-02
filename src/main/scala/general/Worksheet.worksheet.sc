import lib.event.Event
import engine.render.window.AA

val optionInt: Option[Int] = Option(3)
val optionInt2: Option[Int] = None
val optionInt3: Option[Int] = Option.empty

val transformedOptionInt = optionInt.map(_ * 2)
val transformedOptionInt2 = optionInt2.map(_ * 2)
val transformedOptionInt3 = optionInt3.map(_ - 1)

for
  i1 <- optionInt
  i2 <- transformedOptionInt
yield i1 + i2

val e = Event[Unit]
val e2 = Event[Unit]
var msg = ""
var msg2 = ""
e += (_ => msg = "Event 'e' emitted.")
e2 += (_ => msg2 = "Event 'e2' emitted.")
e.emit(())
e2.emit()
msg
msg2

val e3 = new Event[Int]
e3 += (i => msg = i.toString)
e3.emit(3)
msg
