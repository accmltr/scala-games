package engine.render.window

enum AA(val x: Int) {
  case None extends AA(0)
  case x2 extends AA(2)
  case x4 extends AA(4)
  case x8 extends AA(8)
  case x16 extends AA(16)
  case x32 extends AA(32)
}
