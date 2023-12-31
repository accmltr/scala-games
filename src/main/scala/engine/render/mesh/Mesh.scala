package engine.render.mesh

import engine.math.shapes.Polygon
import org.lwjgl.BufferUtils
import engine.math.shapes.*
import engine.math.*
import java.nio.{IntBuffer, FloatBuffer}
import engine.Time.current
import engine.math.geometry.Line

final case class Mesh(vertices: Array[Float], indices: Array[Int])

object Mesh {
  def apply(circle: Circle, segments: Int): Mesh = {
    val angle = (2 * pi) / segments
    val halfRadius = circle.radius / 2

    val vertices = (0 to segments + 1)
      .foldLeft(Array[Float]())((acc, i) =>
        acc ++ Array[Float](
          if (i == 0) 0 else halfRadius * cos(angle * i),
          if (i == 0) 0 else halfRadius * sin(angle * i)
        )
      )

    val indices = (1 to segments)
      .foldLeft(Array[Int]())((acc, i) =>
        acc ++ Array[Int](0, i, if (i == segments) 1 else i + 1)
      )

    Mesh(vertices, indices)
  }

  def apply(rectangle: Rectangle): Mesh = {

    val vertices = Array[Float](
      0,
      0,
      rectangle.width,
      0,
      rectangle.width,
      rectangle.height,
      0,
      rectangle.height
    )

    val indices = Array[Int](0, 1, 2, 2, 3, 0)

    Mesh(vertices, indices)
  }

  def apply(polygon: Polygon): Mesh = {
    val vertices = polygon.points
      .foldLeft(Array[Float]())((acc, point) =>
        acc ++ Array[Float](point.x, point.y)
      )

    def buildIndices(
        currentLine: (Int, Int) = (0, 1),
        closedPoints: List[Int] = Nil,
        lines: List[(Int, Int)] = (0 to polygon.points.length by 2)
          .map(index =>
            (index, if index + 1 == polygon.points.length then 0 else index + 1)
          )
          .toList,
        openPoints: List[Int] = (0 to polygon.points.length).toList
    ): Array[Int] = {

      // Find a point that doesn't cross either lines

      val validPoint = openPoints.find((i: Int) => {
        val vert = polygon.points(i)
        val newLine1 = Line(polygon.points(currentLine._1), vert)
        val newLine2 = Line(polygon.points(currentLine._2), vert)

        lines.forall(l =>
          !(
            Line(polygon.points(l._1), polygon.points(l._2))
              .intersects(newLine1)
              &&
                Line(polygon.points(l._1), polygon.points(l._2))
                  .intersects(newLine2)
          )
        ) match {
          case Some(i: Int) => i
          case None         => throw new Exception("No valid point found")
        }

        // Re this

        ???

      })
    }

    val indices = buildIndices()

    // val indices = (1 to polygon.points.length)
    //   .foldLeft(Array[Int]())((acc, i) =>
    //     acc ++ Array[Int](0, i, if (i == polygon.points.length) 1 else i + 1)
    //   )

    Mesh(vertices, indices)
  }

  def apply(ngon: NGon): Mesh = {
    val angle = (2 * pi) / ngon.points
    val halfRadius = ngon.radius / 2

    val vertices = (0 to ngon.points + 1)
      .foldLeft(Array[Float]())((acc, i) =>
        acc ++ Array[Float](
          if (i == 0) 0 else halfRadius * cos(angle * i),
          if (i == 0) 0 else halfRadius * sin(angle * i)
        )
      )

    val indices = (1 to ngon.points)
      .foldLeft(Array[Int]())((acc, i) =>
        acc ++ Array[Int](0, i, if (i == ngon.points) 1 else i + 1)
      )

    Mesh(vertices, indices)
  }
}
