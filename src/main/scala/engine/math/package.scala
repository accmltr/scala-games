package engine

package object math {
  implicit def floatToImplicitFloat(value: Float): ImplicitFloat =
    ImplicitFloat(value)
}
