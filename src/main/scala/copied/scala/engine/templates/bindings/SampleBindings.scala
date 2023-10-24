// package engine.input.binding_sets

// import engine.input.KeyCode
// import engine.input.MouseCode
// import engine.input.Pressable

// /** A good example of how to create your own set of input bindings.
//   */
// enum SampleBindings(val code: Int, val name: String)
//     extends Pressable(code, name) {
//   case up extends SampleBindings(KeyCode.w)
//   case down extends SampleBindings(KeyCode.s)
//   case left extends SampleBindings(KeyCode.a)
//   case right extends SampleBindings(KeyCode.d)
//   case jump extends SampleBindings(KeyCode.space)
//   case sprint extends SampleBindings(KeyCode.left_shift)
//   case crouch extends SampleBindings(KeyCode.left_control)
//   case interact extends SampleBindings(KeyCode.e)
//   case left_click extends SampleBindings(MouseCode.left)
//   case right_click extends SampleBindings(MouseCode.right)
//   case special_click extends SampleBindings(MouseCode.middle)
//   case drop extends SampleBindings(KeyCode.g)
//   case inventory extends SampleBindings(KeyCode.i)

//   def unapply(binding: SampleBindings[T]): Option[T] = Some(binding.code)
// }
