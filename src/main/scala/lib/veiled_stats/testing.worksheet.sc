import lib.veiled_stats.Stat._
import lib.veiled_stats.StatSheet

val statsheet = StatSheet(
  cpower = 1.7123f,
  cspeed = 2.0f,
  health = 3.0f,
  hregen = 4.0f,
  movespeed = 5.0f
)

statsheet + statsheet
statsheet - statsheet
statsheet / 2.32

statsheet * 3
3 * statsheet

val statsheet2 = StatSheet(
  Map(
    CPower -> 1.7123f,
    CSpeed -> 2.0f,
    Health -> 3.0f,
    HRegen -> 4.0f,
    MoveSpeed -> 5.0f
  )
)

statsheet == statsheet2
statsheet.nearEquals(statsheet2)
statsheet.nearEquals(statsheet2, 0.0000001f)

statsheet.toString
statsheet2.toString

statsheet - statsheet2 == StatSheet.empty
(statsheet2 - statsheet).isEmpty

statsheet(Health)
