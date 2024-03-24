package engine.render.shader

import java.nio.FloatBuffer
import java.nio.IntBuffer
import engine.math.{Vector2, Vector3, Vector4, Matrix3, Matrix4}

type Uniform = FloatBuffer | IntBuffer | Array[Float] | Array[Int] | Boolean |
  Float | Int | Double | Vector2 | Vector3 | Vector4 | Matrix3 |
  Matrix4 // | Matrix2
