package lib.level_system

/** Interface for level systems.
  */
trait LevelSystem {
  def expToLevel(exp: Float): Float
  def levelToExp(level: Float): Float
}
