package engine

import java.io.IOException
import java.nio.FloatBuffer
import java.nio.file.{Files, Paths}

package object io {

  def fileExists(path: String): Boolean = Files.exists(Paths.get(path))

  def readTextFile(path: String): Option[String] = {
    if fileExists(path)
    then
      try
        val source = scala.io.Source.fromFile(path)
        val lines = source.getLines
        val result = lines.mkString("\n")
        source.close()
        Option(result)
      catch
        case e: Exception =>
          // throw new IOException(s"Error reading file: $e")
          None
    else
      // throw new IOException(s"File does not exist: $path")
      None
  }
}
