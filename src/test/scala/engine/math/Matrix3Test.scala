package engine.math

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Matrix3Spec extends AnyFlatSpec with Matchers {

  "A Matrix3" should "be an identity matrix when constructed with no arguments" in {
    val m = Matrix3.IDENTITY
    m should be(Matrix3(1, 0, 0, 0, 1, 0, 0, 0, 1))
  }

  it should "correctly apply a translation" in {
    val m = Matrix3(Vector2(1, 2), 0, 1)
    m should be(Matrix3(1, 0, 1, 0, 1, 2, 0, 0, 1))
  }

  it should "correctly apply a rotation" in {
    val m = Matrix3(Vector2(0, 0), Math.PI.toFloat / 2, 1)
    val result = m * Vector2(1, 0)
    result.x should be(0.0f +- 0.001f)
    result.y should be(1.0f +- 0.001f)
  }

  it should "correctly apply a scale" in {
    val m = Matrix3(Vector2(0, 0), 0, 2)
    val result = m * Vector2(1, 0)
    result.x should be(2.0f +- 0.001f)
    result.y should be(0.0f +- 0.001f)
  }
}
