package engine.render.window

import engine.math.Vector2

final case class ScreenSize(width: Int, height: Int) {
  def toVector2: Vector2 = Vector2(width, height)
}

object ScreenSize {

  inline def p480: ScreenSize = ScreenSize(640, 480)
  inline def p720: ScreenSize = ScreenSize(1280, 720)
  inline def p1080: ScreenSize = ScreenSize(1920, 1080)
  inline def p1440: ScreenSize = ScreenSize(2560, 1440)
  inline def p2160: ScreenSize = ScreenSize(3840, 2160)
  inline def p4320: ScreenSize = ScreenSize(7680, 4320)
  inline def vga: ScreenSize = p480
  inline def hd: ScreenSize = p720
  inline def fhd: ScreenSize = p1080
  inline def twoK: ScreenSize = p1440
  inline def fourK: ScreenSize = p2160
  inline def eightK: ScreenSize = p4320

  inline def wide_p480: ScreenSize = ScreenSize(852, 480)
  inline def wide_p720: ScreenSize = ScreenSize(1366, 768)
  inline def wide_p1080: ScreenSize = ScreenSize(1920, 1080)
  inline def wide_p1440: ScreenSize = ScreenSize(2560, 1440)
  inline def wide_p2160: ScreenSize = ScreenSize(3840, 2160)
  inline def wide_p4320: ScreenSize = ScreenSize(7680, 4320)

  inline def wide_vga: ScreenSize = wide_p480
  inline def wide_hd: ScreenSize = wide_p720
  inline def wide_fhd: ScreenSize = wide_p1080
  inline def wide_twoK: ScreenSize = wide_p1440
  inline def wide_fourK: ScreenSize = wide_p2160
  inline def wide_eightK: ScreenSize = wide_p4320

}
