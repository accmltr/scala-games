package engine.input

/*
#define 	GLFW_MOUSE_BUTTON_1   0
#define 	GLFW_MOUSE_BUTTON_2   1
#define 	GLFW_MOUSE_BUTTON_3   2
#define 	GLFW_MOUSE_BUTTON_4   3
#define 	GLFW_MOUSE_BUTTON_5   4
#define 	GLFW_MOUSE_BUTTON_6   5
#define 	GLFW_MOUSE_BUTTON_7   6
#define 	GLFW_MOUSE_BUTTON_8   7
 */

/** Enum for the mouse button codes.
  *
  * @param code
  *   The button's corresponding GLFW mouse button code.
  */
enum MouseCode(val code: Int, val name: String) extends Pressable(code, name) {
  case unknown extends MouseCode(-1, "UNKNOWN")
  case left extends MouseCode(0, "lmb")
  case right extends MouseCode(1, "rmb")
  case middle extends MouseCode(2, "mmb")
  case back extends MouseCode(3, "mouse_back/mb4")
  case forward extends MouseCode(4, "mouse_forward/mb5")
  case button_4 extends MouseCode(3, "mouse_back/mb4")
  case button_5 extends MouseCode(4, "mouse_forward/mb5")
  case button_6 extends MouseCode(5, "mb6")
  case button_7 extends MouseCode(6, "mb7")
  case button_8 extends MouseCode(7, "mb8")
}

object MouseCode {

  /** Returns the `MouseCode` enum value from the given code.
    *
    * @param code
    *   The button's corresponding GLFW mouse button code.
    * @return
    *   The enum value for the given code.
    */
  def from(code: Int): MouseCode =
    MouseCode.values
      .find(_.code == code)
      .getOrElse(MouseCode.unknown)
}
