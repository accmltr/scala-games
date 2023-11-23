package lib.level_system

object Basic extends LevelSystem {

  private def nonLinearExpToLevel(exp: Float): Float = {
    math.floor(1 + (math.sqrt(exp / 100))).toFloat
  }

  override def expToLevel(exp: Float): Float = {
    val level = nonLinearExpToLevel(exp)
    val levelExp = levelToExp(level)
    val nextLevelExp = levelToExp(level + 1)
    val levelProgress = (exp - levelExp) / (nextLevelExp - levelExp)
    level + levelProgress
  }

  private def nonLinearLevelToExp(level: Float): Float = {
    math.pow(level - 1, 2).toFloat * 100
  }

  override def levelToExp(level: Float): Float = {
    val levelFloor = math.floor(level).toFloat
    val levelProgress = level - levelFloor
    val levelExp = nonLinearLevelToExp(levelFloor)
    val nextLevelExp = nonLinearLevelToExp(levelFloor + 1)
    levelExp + levelProgress * (nextLevelExp - levelExp)
  }

}
