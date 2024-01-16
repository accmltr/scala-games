package engine.render

import java.nio.FloatBuffer
import java.nio.IntBuffer
import engine.math.Vector3
import engine.math.Vector2
import engine.math.Matrix3
import engine.math.Matrix4
import engine.math.Vector4

package object shader {
  // TODO: Implement new uniform types
  type Uniform = FloatBuffer | IntBuffer | Array[Float] | Array[Int] | Boolean |
    Float | Int | Double | Vector2 | Vector3 | Vector4 | Matrix3 |
    Matrix4 // | Texture | Matrix2
}
