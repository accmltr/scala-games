trait Container {
  type T = Any
  val contents: Any
}

class A extends Container {

  override val contents: String = "Hello"

}
class B extends Container {

  override val contents: Int = 3874

}

val a = new A
println(a.contents)

val b = new B
println(b.contents)
