package engine.render.window

import engine.math.Vector2

final case class ScreenSize(width: Int, height: Int)

object ScreenSize {

  val p480: ScreenSize = ScreenSize(640, 480)
  val p720: ScreenSize = ScreenSize(1280, 720)
  val p1080: ScreenSize = ScreenSize(1920, 1080)
  val p1440: ScreenSize = ScreenSize(2560, 1440)
  val p2160: ScreenSize = ScreenSize(3840, 2160)
  val p4320: ScreenSize = ScreenSize(7680, 4320)
  val vga: ScreenSize = p480
  val hd: ScreenSize = p720
  val fhd: ScreenSize = p1080
  val twoK: ScreenSize = p1440
  val fourK: ScreenSize = p2160
  val eightK: ScreenSize = p4320

  val wide_p480: ScreenSize = ScreenSize(852, 480)
  val wide_p720: ScreenSize = ScreenSize(1366, 768)
  val wide_p1080: ScreenSize = ScreenSize(1920, 1080)
  val wide_p1440: ScreenSize = ScreenSize(2560, 1440)
  val wide_p2160: ScreenSize = ScreenSize(3840, 2160)
  val wide_p4320: ScreenSize = ScreenSize(7680, 4320)

  val wide_vga: ScreenSize = wide_p480
  val wide_hd: ScreenSize = wide_p720
  val wide_fhd: ScreenSize = wide_p1080
  val wide_twoK: ScreenSize = wide_p1440
  val wide_fourK: ScreenSize = wide_p2160
  val wide_eightK: ScreenSize = wide_p4320

}

object ScreenSizeImplicits {
  // (Int, Int) -> ScreenSize
  given Conversion[(Int, Int), ScreenSize] with
    def apply(size: (Int, Int)): ScreenSize = ScreenSize(size._1, size._2)

  // Vector2 -> ScreenSize
  given Conversion[Vector2, ScreenSize] with
    def apply(size: Vector2): ScreenSize =
      ScreenSize(size.x.toInt, size.y.toInt)

  // ScreenSize -> Vector2
  given Conversion[ScreenSize, Vector2] with
    def apply(size: ScreenSize): Vector2 = Vector2(size.width, size.height)
}
