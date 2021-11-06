ThisBuild / scalaVersion := "3.1.0"
ThisBuild / organization := "com.github.esgott"

lazy val root = (project in file("."))
  .settings(
    name := "text-search",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
  )
