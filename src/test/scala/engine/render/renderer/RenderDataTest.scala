package engine.render.renderer

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import engine.test_utils.assertNearEquals
import engine.math.{Vector2, Vector4}
import engine.math.shapes.Polygon
import engine.render.shader.Uniform
import engine.render.Color
import engine.math.Matrix3

class RenderDataTest extends AnyFlatSpec {
  "RenderData" should "throw an exception if a reserved uniform name is used in 'extraUniforms'" in {
    assertThrows[Exception] {
      render_data.RenderData.fromPolygon(
        polygon = Polygon(
          Vector(Vector2(0, 0), Vector2(0, 1), Vector2(1, 1), Vector2(1, 0))
        ),
        extraUniforms = Map("uRes" -> Vector2(10, 10))
      )
    }
    assertThrows[Exception] {
      render_data.RenderData.fromPolygon(
        polygon = Polygon(
          Vector(Vector2(0, 0), Vector2(0, 1), Vector2(1, 1), Vector2(1, 0))
        ),
        extraUniforms = Map("uLayer" -> 0)
      )
    }
    assertThrows[Exception] {
      render_data.RenderData.fromPolygon(
        polygon = Polygon(
          Vector(Vector2(0, 0), Vector2(0, 1), Vector2(1, 1), Vector2(1, 0))
        ),
        extraUniforms = Map("uColor" -> Vector4(0, 0, 0, 0))
      )
    }
    assertThrows[Exception] {
      render_data.RenderData.fromPolygon(
        polygon = Polygon(
          Vector(Vector2(0, 0), Vector2(0, 1), Vector2(1, 1), Vector2(1, 0))
        ),
        extraUniforms = Map("uTrans" -> Matrix3.IDENTITY)
      )
    }

  }
  it should "not thow an exception if non-reserved uniforms are added to 'extraUniforms" in {
    render_data.RenderData.fromPolygon(
      polygon = Polygon(
        Vector(Vector2(0, 0), Vector2(0, 1), Vector2(1, 1), Vector2(1, 0))
      ),
      extraUniforms = Map("nonReservedUniform" -> Matrix3.IDENTITY)
    )
  }
}
