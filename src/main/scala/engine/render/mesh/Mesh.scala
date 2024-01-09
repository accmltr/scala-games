package engine.render.mesh

import engine.math.shapes.Polygon
import org.lwjgl.BufferUtils
import engine.math.shapes.*
import engine.math.*
import java.nio.{IntBuffer, FloatBuffer}
import engine.Time.current
import engine.math.geometry.Line
import scala.util.boundary, boundary.break

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

    def lineFromIndices(i: (Int, Int)): Line = {
      Line(polygon.points(i._1), polygon.points(i._2))
    }

    def linesEq(l1: (Int, Int), l2: (Int, Int)): Boolean = {
      l1 == l2 || l1 == (l2._2, l2._1)
    }

    def isLineInTri(l: (Int, Int), t: (Int, Int, Int)): Boolean = {
      linesEq(l, (t._1, t._2)) ||
      linesEq(l, (t._2, t._3)) ||
      linesEq(l, (t._3, t._1))
    }

    def trisEq(t1: (Int, Int, Int), t2: (Int, Int, Int)): Boolean = {
      isLineInTri((t1._1, t1._2), t2) &&
      isLineInTri((t1._2, t1._3), t2) &&
      isLineInTri((t1._3, t1._1), t2)
    }

    def overlapsWithLines(
        l: (Int, Int),
        lines: List[(Int, Int)]
    ): Boolean = {
      boundary[Boolean]:
        for cl <- lines
        do
          val l1 = lineFromIndices(l)
          val l2 = lineFromIndices(cl)
          if l1.overlaps(l2, false)
          then break(true)
        false
    }

    def overlapsWithTris(
        l: (Int, Int),
        triangles: List[(Int, Int, Int)]
    ): Boolean = {
      val line = lineFromIndices(l)
      boundary[Boolean]:
        for
          t <- triangles
          tl <- List((t._1, t._2), (t._2, t._3), (t._3, t._1))
            .map(lineFromIndices)
        do
          if line.overlaps(tl, false)
          then break(true)
        false
    }

    /** @param i
      *   Index of outline point of polygon in question.
      * @param to
      *   New point to create line in question.
      * @return
      *   `true` when line points into the polygon. (Does not check if line
      *   strikes through the polygon's other side.)
      */
    def angledInwards(i: Int, p: Int): Boolean = {

      val ai =
        if (i - 1) == -1
        then polygon.points.size - 1
        else i - 1
      val bi =
        if (i + 1) == polygon.points.size
        then 0
        else i + 1

      val a = lineFromIndices(i, ai).angle
      val k = lineFromIndices(i, p).angle
      val b = lineFromIndices(i, bi).angle

      engine.math.angleInBounds(
        k,
        b,
        a,
        polygon.isClockwise
      )

    }

    def buildIndices(
        lines: List[(Int, Int)],
        triangles: List[(Int, Int, Int)]
    ): Array[Int] = {

      if lines.isEmpty
      then
        triangles.foldLeft(Array[Int]())((acc, t) =>
          acc ++ Array[Int](t._1, t._2, t._3)
        )
      else
        val li = lines.head
        val l = lineFromIndices(li)

        boundary[Array[Int]]:
          // Try to form a valid triangle
          for p <- 0 until polygon.points.length if !(p == li._1 || p == li._2)
          do
            // Useful Locals
            val l1i = (li._1, p)
            val l2i = (li._2, p)
            val l1 = lineFromIndices(l1i)
            val l2 = lineFromIndices(l2i)
            val tri = (li._1, li._2, p)

            val triExists = !triangles.forall(!trisEq(tri, _))

            if triExists
            then break(buildIndices(lines.tail, triangles))
            else

              // Check if lines already exist within 'triangles' or 'lines'
              val l1Exists = {
                val existsInLines = !lines.forall(!linesEq(l1i, _))
                val existsInTris = !triangles.forall(!isLineInTri(l1i, _))
                existsInLines || existsInTris
              }
              val l2Exists = {
                val existsInLines = !lines.forall(!linesEq(l2i, _))
                val existsInTris = !triangles.forall(!isLineInTri(l2i, _))
                existsInLines || existsInTris
              }

              // Validity checks
              val l1Valid =
                if l1Exists
                then true
                else
                  !overlapsWithTris(l1i, triangles) &&
                  !overlapsWithLines(l1i, lines) &&
                  angledInwards(li._1, p)
              val l2Valid =
                if l2Exists
                then true
                else
                  !overlapsWithTris(l2i, triangles) &&
                  !overlapsWithLines(l2i, lines) &&
                  angledInwards(li._2, p)

              // Valid recursion
              if l1Valid && l2Valid
              then break(buildIndices(lines.tail, tri :: triangles))

          // Throw and error if none found
          throw new Exception("Polygon could not be triangulated")
    }

    // def buildIndices(
    //     currentLine: (Int, Int) = (0, 1),
    //     // usedPoints: List[Int] = List(1, 0),
    //     lines: List[(Int, Int)] = (0 until polygon.points.length)
    //       .map(index =>
    //         (index, if index + 1 == polygon.points.length then 0 else index + 1)
    //       )
    //       .toList,
    //     openPoints: List[Int] = (2 until polygon.points.length).toList,
    //     allPoints: List[Int] = (0 until polygon.points.length).toList
    // ): Array[Int] = {
    //   val usedPoints = lines
    //     .foldLeft(Array[Int]())((acc, line) =>
    //       acc ++ Array[Int](line._1, line._2)
    //     )
    //     .distinct
    //   // val openPoints = allPoints

    //   // Stop if
    //   if openPoints.isEmpty
    //   then
    //     lines
    //       .foldLeft(Array[Int]())((acc, line) =>
    //         acc ++ Array[Int](line._1, line._2)
    //       )
    //   else
    //     // Find a point that does not cross either lines of potential triangle formed between currentLine and the point
    //     allPoints.find((i: Int) => {

    //       // Skip point if it already forms a triangle with currentLine
    //       val newLine1Exists = lines.exists(l =>
    //         (l._1 == currentLine._1 && l._2 == i) ||
    //           (l._1 == i && l._2 == currentLine._1)
    //       )
    //       val newLine2Exists = lines.exists(l =>
    //         (l._1 == currentLine._2 && l._2 == i) ||
    //           (l._1 == i && l._2 == currentLine._2)
    //       )
    //       if newLine1Exists && newLine2Exists
    //       then true
    //       else if !(currentLine._1 == i || currentLine._2 == i)
    //       then
    //         val newLine1 =
    //           Line(polygon.points(currentLine._1), polygon.points(i))
    //         val newLine2 =
    //           Line(polygon.points(currentLine._2), polygon.points(i))

    //         var connectedLines = lines.filter(l =>
    //           l._1 == currentLine._1 || l._2 == currentLine._1 ||
    //             l._1 == currentLine._2 || l._2 == currentLine._2
    //         )

    //         lines.forall(l =>
    //           val line = Line(polygon.points(l._1), polygon.points(l._2))
    //           if l == currentLine || connectedLines.contains(l)
    //           then
    //             !(line.overlaps(newLine1, false) || line
    //               .overlaps(newLine2, false))
    //           else
    //             !(line.intersects(newLine1, false) || line
    //               .intersects(newLine2, false))
    //         ) &&
    //         polygon.points.forall(p =>
    //           !(newLine2.contains(p, false) || newLine1.contains(p, false))
    //         )
    //       else false
    //     }) match
    //       case None =>
    //         buildIndices(
    //           (
    //             currentLine._2,
    //             if currentLine._2 + 1 == polygon.points.length then 0
    //             else currentLine._2 + 1
    //           ),
    //           // usedPoints,
    //           lines,
    //           openPoints.filterNot(p =>
    //             p == currentLine._1 || p == currentLine._2
    //           ),
    //           allPoints
    //         )
    //       case Some(i) =>
    //         buildIndices(
    //           (
    //             currentLine._2,
    //             if currentLine._2 + 1 == polygon.points.length then 0
    //             else currentLine._2 + 1
    //           ),
    //           // i :: usedPoints,
    //           {
    //             val filtered = lines.filterNot(l =>
    //               (((l._1 == currentLine._1 && l._2 == i) ||
    //                 (l._1 == i && l._2 == currentLine._1)) ||
    //                 ((l._1 == currentLine._2 && l._2 == i) ||
    //                   (l._1 == i && l._2 == currentLine._2)))
    //             )
    //             val additional = List(
    //               (currentLine._1, i),
    //               (currentLine._2, i)
    //             )
    //             val res = filtered ++ additional
    //             res
    //           },
    //           openPoints.filterNot(p =>
    //             p == i || p == currentLine._1 || p == currentLine._2
    //           ),
    //           allPoints
    //         )
    // }

    val indices = buildIndices(
      (0 until polygon.points.length)
        .map(index =>
          (index, if index + 1 == polygon.points.length then 0 else index + 1)
        )
        .toList,
      Nil
    )

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
