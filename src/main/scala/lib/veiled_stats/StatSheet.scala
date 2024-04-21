package lib.veiled_stats

import engine.math.NearEqualsable
import engine.math.given_Conversion_Float_ImplicitFloat

final case class StatSheet(
    val cpower: Float = 0.0f,
    val cspeed: Float = 0.0f,
    val health: Float = 0.0f,
    val hregen: Float = 0.0f,
    val movespeed: Float = 0.0f
) extends NearEqualsable[StatSheet] {

  val isEmpty: Boolean = this == StatSheet.empty

  def apply(stat: Stat): Float = stat match {
    case Stat.CPower    => cpower
    case Stat.CSpeed    => cspeed
    case Stat.Health    => health
    case Stat.HRegen    => hregen
    case Stat.MoveSpeed => movespeed
  }

  def +(other: StatSheet): StatSheet = {
    StatSheet(
      cpower = cpower + other.cpower,
      cspeed = cspeed + other.cspeed,
      health = health + other.health,
      hregen = hregen + other.hregen,
      movespeed = movespeed + other.movespeed
    )
  }

  def -(other: StatSheet): StatSheet = {
    StatSheet(
      cpower = cpower - other.cpower,
      cspeed = cspeed - other.cspeed,
      health = health - other.health,
      hregen = hregen - other.hregen,
      movespeed = movespeed - other.movespeed
    )
  }

  def *(scalar: Float): StatSheet = {
    StatSheet(
      cpower = cpower * scalar,
      cspeed = cspeed * scalar,
      health = health * scalar,
      hregen = hregen * scalar,
      movespeed = movespeed * scalar
    )
  }

  def /(scalar: Float): StatSheet = {
    StatSheet(
      cpower = cpower / scalar,
      cspeed = cspeed / scalar,
      health = health / scalar,
      hregen = hregen / scalar,
      movespeed = movespeed / scalar
    )
  }

  def nearEquals(other: StatSheet, epsilon: Float = 0.0001f): Boolean = {
    cpower.nearEquals(other.cpower, epsilon) &&
      cspeed.nearEquals(other.cspeed, epsilon) &&
      health.nearEquals(other.health, epsilon) &&
      hregen.nearEquals(other.hregen, epsilon) &&
      movespeed.nearEquals(other.movespeed, epsilon)
  }

  override def toString: String = {
    s"Stats(cpower = $cpower, cspeed = $cspeed, health = $health, hregen = $hregen, movespeed = $movespeed)"
  }
}

object StatSheet {

  val empty: StatSheet = StatSheet()

  def apply(values: Map[Stat, Float]): StatSheet = {
    StatSheet(
      cpower = values.getOrElse(Stat.CPower, 0.0f),
      cspeed = values.getOrElse(Stat.CSpeed, 0.0f),
      health = values.getOrElse(Stat.Health, 0.0f),
      hregen = values.getOrElse(Stat.HRegen, 0.0f),
      movespeed = values.getOrElse(Stat.MoveSpeed, 0.0f)
    )
  }

  implicit class IntOps(val i: Int) extends AnyVal {
    def *(s: StatSheet): StatSheet = s * i
  }
}
