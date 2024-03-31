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
