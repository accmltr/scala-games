package engine

import java.io.IOException
import java.nio.FloatBuffer
import java.nio.file.{Files, Paths}

package object io {
  def readTextFile(path: String): String = {
    if (Files.exists(Paths.get(path))) {
      try {
        val source = scala.io.Source.fromFile(path)
        val lines = source.getLines
        val result = lines.mkString("\n")
        source.close()
        result
      } catch {
        case e: Exception =>
          throw new IOException(s"Error reading shader file: $e")
      }
    } else {
      throw new IOException(s"Shader file does not exist: $path")
    }
  }
}
