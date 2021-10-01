import sbt.Keys._
import sbt._

lazy val `opening-hours` = (project in file("."))
  .settings(
    organization := "io.github.viktor-podzigun",
    name := "opening-hours",
    description := "Coding task: REST API Service for formatting opening hours of restaurants",

    scalaVersion := "2.13.6",
    scalacOptions ++= Seq(
      //see https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
      //"-Xcheckinit",
      "-Xfatal-warnings",
      "-Xlint:_",
      "-Ywarn-macros:after", // Only inspect expanded trees when generating unused symbol warnings
      "-explaintypes",
      "-unchecked",
      "-deprecation",
      "-feature"
    ),

    libraryDependencies ++= {
      Dependencies.http4s ++
        Dependencies.circe ++
        Dependencies.cats ++
        Dependencies.logging ++ Seq(
          TestDependencies.scalaTest
        ).map(_ % Test)
    }
  )
