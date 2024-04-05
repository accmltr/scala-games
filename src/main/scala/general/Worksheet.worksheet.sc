trait Container {
  type T = Any
  val contents: Any
}

class Animal(val name: String)
class Fish(name: String) extends Animal(name)

class A extends Container {

  override val contents: Animal = new Animal("Animal")

}

class AA extends A {
  override val contents: Fish = new Fish("Fish")
}

class B extends Container {

  override val contents: Int = 3874

}

val a = new A
println(a.contents.name)

val b = new B
println(b.contents)

val aa = new AA
println(aa.contents.name)
