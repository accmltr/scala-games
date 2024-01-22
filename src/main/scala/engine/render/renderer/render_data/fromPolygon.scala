package engine.render.renderer.render_data

import engine.math.shapes.Polygon
import scala.util.boundary, boundary.break
import engine.math.geometry.Line

private[render_data] def vertsAndIndicesFromPolygon(
    polygon: Polygon
): (Array[Float], Array[Int]) = {
  (verticesFromPolygon(polygon), indicesFromPolygon(polygon))
}

private def verticesFromPolygon(polygon: Polygon): Array[Float] =
  polygon.points
    .foldLeft(Array[Float]())((acc, point) =>
      acc ++ Array[Float](point.x, point.y)
    )

private def indicesFromPolygon(polygon: Polygon): Array[Int] = {

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
      lines: List[(Int, Int)] = (0 until polygon.points.length)
        .map(index =>
          (
            index,
            if index + 1 == polygon.points.length then 0 else index + 1
          )
        )
        .toList
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

  def findValidPoint(
      remaining: List[(Int, Int)],
      triangles: List[(Int, Int, Int)]
  ): Option[Int] = {
    val li = remaining.head
    val l = lineFromIndices(li)

    boundary[Option[Int]]:
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
        then break(Option(p))
        else

          // Check if lines already exist within 'triangles' or 'lines'
          val l1Exists = {
            val existsInLines = !remaining.forall(!linesEq(l1i, _))
            val existsInTris = !triangles.forall(!isLineInTri(l1i, _))
            existsInLines || existsInTris
          }
          val l2Exists = {
            val existsInLines = !remaining.forall(!linesEq(l2i, _))
            val existsInTris = !triangles.forall(!isLineInTri(l2i, _))
            existsInLines || existsInTris
          }

          // Validity checks
          val l1Valid =
            if l1Exists
            then true
            else
              !overlapsWithTris(l1i, triangles) &&
              !overlapsWithLines(l1i, remaining) &&
              angledInwards(li._1, p)
          val l2Valid =
            if l2Exists
            then true
            else
              !overlapsWithTris(l2i, triangles) &&
              !overlapsWithLines(l2i, remaining) &&
              angledInwards(li._2, p)

          // Valid recursion
          if l1Valid && l2Valid
          then break(Option(p))
      break(None)
  }

  def findConnectingEdges(
      tri: (Int, Int, Int),
      p: Int
  ): Option[List[((Int, Int))]] = {
    if (tri._1 == 0)
      Option(List((tri._1, tri._2), (tri._1, tri._3)))
    else if (tri._2 == 0)
      Option(List((tri._2, tri._1), (tri._2, tri._3)))
    else if (tri._3 == 0)
      Option(List((tri._3, tri._1), (tri._3, tri._2)))
    else None
  }

  def buildIndices(
      remaining: List[(Int, Int)],
      triangles: List[(Int, Int, Int)]
  ): Array[Int] = {

    if remaining.isEmpty
    then

      val result = // Convert triangles to indices to return final result for 'buildIndices'
        triangles.foldLeft(Array[Int]())((acc, t) =>
          acc ++ Array[Int](t._1, t._2, t._3)
        )

      // Close last triangle if 2 inner lines exist between 1st and last outline lines
      val outlinesToConsider =
        List((0, 1), (polygon.points.length - 1, 0))
      val innerLines = triangles
        .flatMap(findConnectingEdges(_, 0))
        .flatten
        .filter(innerLine =>
          outlinesToConsider.forall(outterLine =>
            !linesEq(outterLine, innerLine)
          )
        )

      if innerLines.length == 2
      then
        findValidPoint(List(innerLines.head), triangles) match
          case Some(p) =>
            val tri = (innerLines.head._1, innerLines.head._2, p)
            val triExists = !triangles.forall(!trisEq(tri, _))
            if triExists
            then result
            else result ++ Array[Int](tri._1, tri._2, tri._3)
          case None =>
            throw new Exception("Polygon could not be triangulated")
      else result
    else

      // Find a valid point to create a new triangle
      findValidPoint(remaining, triangles) match
        case Some(p) =>
          val li = remaining.head
          val tri = (li._1, li._2, p)
          val triExists = !triangles.forall(!trisEq(tri, _))
          if triExists
          then buildIndices(remaining.tail, triangles)
          else buildIndices(remaining.tail, tri :: triangles)
        case None =>
          throw new Exception("Polygon could not be triangulated")
  }

  // indices
  buildIndices(
    (0 until polygon.points.length)
      .map(index =>
        (index, if index + 1 == polygon.points.length then 0 else index + 1)
      )
      .toList,
    Nil
  )

}
