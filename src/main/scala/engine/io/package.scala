package engine

import java.io.IOException
import java.nio.FloatBuffer
import java.nio.file.{Files, Paths}

package object io {
  def readTextFile(path: String): String = {
    try {
      new String(Files.readAllBytes(Paths.get(path)))
    } catch {
      case e: IOException =>
        e.printStackTrace()
        assert(false, s"Error: Could read file: '$path'")
    }
  }
}
