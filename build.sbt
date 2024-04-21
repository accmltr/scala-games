ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.1"

lazy val root = (project in file("."))
  .settings(
    name := "scala_games",
    //    idePackagePrefix := Some("com.scala_games")
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test"

val lwjglVersion = "3.3.3"
val jomlVersion = "1.10.5"

val nativeOS = System.getProperty("os.name").toLowerCase match {
  case x if x.contains("win") => "natives-windows"
  case x if x.contains("mac") => "natives-macos"
  case _ => "natives-linux"
}

libraryDependencies ++= Seq(
  "org.lwjgl" % "lwjgl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
  "org.lwjgl" % "lwjgl-nfd" % lwjglVersion,
  "org.lwjgl" % "lwjgl-openal" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-stb" % lwjglVersion,
  "org.lwjgl" % "lwjgl" % lwjglVersion classifier nativeOS,
  "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier nativeOS,
  "org.lwjgl" % "lwjgl-nfd" % lwjglVersion classifier nativeOS,
  "org.lwjgl" % "lwjgl-openal" % lwjglVersion classifier nativeOS,
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier nativeOS,
  "org.lwjgl" % "lwjgl-stb" % lwjglVersion classifier nativeOS,
  "org.joml" % "joml" % jomlVersion,
  "io.github.spair" % "imgui-java-app" % "1.86.11",
  "org.dyn4j" % "dyn4j" % "5.0.2",
)