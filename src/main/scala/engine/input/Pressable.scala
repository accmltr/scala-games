package engine.input

trait Pressable(code: Int, name: String) {
  override def toString(): String = name
}
