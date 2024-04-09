import lib.property.Property

class Character(val name: String, val maxHp: Int) {
  val hp = Property(
    maxHp,
    i =>
      require(
        i <= maxHp && i >= 0,
        "Cannot set hp to higher than max or lower than 0"
      )
      i
  )
}

val wolf = new Character("Wolf", 70)

wolf.hp.value

wolf.hp.onChange += ((o, n) => println(s"hp changed from $o to $n"))

wolf.hp.value = 54

wolf.hp.value

wolf.hp.value = 71
