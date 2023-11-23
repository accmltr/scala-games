package lib.level_system

object Basic extends LevelSystem {

  override def expToLevel(exp: Float): Float = {
    1 + (math.sqrt(exp / 100).toFloat)
  }

  override def levelToExp(level: Float): Float = {
    math.pow(level, 2).toFloat * 100
  }

}
