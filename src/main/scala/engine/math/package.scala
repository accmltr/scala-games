package engine

package object math {
  // ------------------------
  // constants
  // ------------------------
  val pi: Float = scala.math.Pi.toFloat

  // ------------------------
  // implicit conversions
  // ------------------------

  given Conversion[Float, extended_primitives.ImplicitFloat] =
    extended_primitives.ImplicitFloat(_)
}
