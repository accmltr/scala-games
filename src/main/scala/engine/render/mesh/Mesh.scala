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
        // usedPoints: List[Int] = List(1, 0),
        lines: List[(Int, Int)] = (0 until polygon.points.length)
          .map(index =>
            (index, if index + 1 == polygon.points.length then 0 else index + 1)
          )
          .toList,
        openPoints: List[Int] = (2 until polygon.points.length).toList,
        allPoints: List[Int] = (0 until polygon.points.length).toList
    ): Array[Int] = {
      val usedPoints = lines
        .foldLeft(Array[Int]())((acc, line) =>
          acc ++ Array[Int](line._1, line._2)
        )
        .distinct
      // val openPoints = allPoints

      // Stop if
      if openPoints.isEmpty
      then
        lines
          .foldLeft(Array[Int]())((acc, line) =>
            acc ++ Array[Int](line._1, line._2)
          )
      else
        // Find a point that does not cross either lines of potential triangle formed between currentLine and the point
        allPoints.find((i: Int) => {

          // Skip point if it already forms a triangle with currentLine
          val newLine1Exists = lines.exists(l =>
            (l._1 == currentLine._1 && l._2 == i) ||
              (l._1 == i && l._2 == currentLine._1)
          )
          val newLine2Exists = lines.exists(l =>
            (l._1 == currentLine._2 && l._2 == i) ||
              (l._1 == i && l._2 == currentLine._2)
          )
          if newLine1Exists && newLine2Exists
          then true
          else if !(currentLine._1 == i || currentLine._2 == i)
          then
            val newLine1 =
              Line(polygon.points(currentLine._1), polygon.points(i))
            val newLine2 =
              Line(polygon.points(currentLine._2), polygon.points(i))

            var connectedLines = lines.filter(l =>
              l._1 == currentLine._1 || l._2 == currentLine._1 ||
                l._1 == currentLine._2 || l._2 == currentLine._2
            )

            lines.forall(l =>
              val line = Line(polygon.points(l._1), polygon.points(l._2))
              if l == currentLine || connectedLines.contains(l)
              then
                !(line.overlaps(newLine1, false) || line
                  .overlaps(newLine2, false))
              else
                !(line.intersects(newLine1, false) || line
                  .intersects(newLine2, false))
            ) &&
            polygon.points.forall(p =>
              !(newLine2.contains(p, false) || newLine1.contains(p, false))
            )
          else false
        }) match
          case None => throw new Exception("Invalid polygon")
          case Some(i) =>
            buildIndices(
              (
                currentLine._2,
                if currentLine._2 + 1 == polygon.points.length then 0
                else currentLine._2 + 1
              ),
              // i :: usedPoints,
              {
                val filtered = lines.filterNot(l =>
                  (((l._1 == currentLine._1 && l._2 == i) ||
                    (l._1 == i && l._2 == currentLine._1)) ||
                    ((l._1 == currentLine._2 && l._2 == i) ||
                      (l._1 == i && l._2 == currentLine._2)))
                )
                val additional = List(
                  (currentLine._1, i),
                  (currentLine._2, i)
                )
                val res = filtered ++ additional
                res
              },
              openPoints.filterNot(p =>
                p == i || p == currentLine._1 || p == currentLine._2
              ),
              allPoints
            )
    }

    Mesh(vertices, buildIndices())
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
