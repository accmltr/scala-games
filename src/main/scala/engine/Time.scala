package engine

object Time {
  val start: Long = System.nanoTime()

  def current: Float = ((System.nanoTime() - start) * 1e-9).toFloat

}

object TimeImplicits {
  given Conversion[Time.type, Float] = (time: Time.type) => time.current
}
