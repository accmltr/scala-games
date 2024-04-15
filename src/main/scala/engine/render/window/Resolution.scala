package engine.render.window

import engine.math.Vector2

final case class Resolution(width: Int, height: Int) {
  def toVector2: Vector2 = Vector2(width, height)
}

object Resolution {

  inline def p480: Resolution = Resolution(640, 480)
  inline def p720: Resolution = Resolution(1280, 720)
  inline def p1080: Resolution = Resolution(1920, 1080)
  inline def p1440: Resolution = Resolution(2560, 1440)
  inline def p2160: Resolution = Resolution(3840, 2160)
  inline def p4320: Resolution = Resolution(7680, 4320)
  inline def vga: Resolution = p480
  inline def hd: Resolution = p720
  inline def fhd: Resolution = p1080
  inline def twoK: Resolution = p1440
  inline def fourK: Resolution = p2160
  inline def eightK: Resolution = p4320

  inline def wide_p480: Resolution = Resolution(852, 480)
  inline def wide_p720: Resolution = Resolution(1366, 768)
  inline def wide_p1080: Resolution = Resolution(1920, 1080)
  inline def wide_p1440: Resolution = Resolution(2560, 1440)
  inline def wide_p2160: Resolution = Resolution(3840, 2160)
  inline def wide_p4320: Resolution = Resolution(7680, 4320)

  inline def wide_vga: Resolution = wide_p480
  inline def wide_hd: Resolution = wide_p720
  inline def wide_fhd: Resolution = wide_p1080
  inline def wide_twoK: Resolution = wide_p1440
  inline def wide_fourK: Resolution = wide_p2160
  inline def wide_eightK: Resolution = wide_p4320

}
