import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.docker.Cmd
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.universal.UniversalDeployPlugin
import sbt.Keys._
import sbt._

lazy val `opening-hours` = (project in file("."))
  .enablePlugins(
    JavaAppPackaging,
    UniversalDeployPlugin
  )
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
      //"-Xlint:-byname-implicit",
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
        Dependencies.swagger ++
        Dependencies.logging ++ Seq(
          TestDependencies.scalaTest
        ).map(_ % Test)
    },

    //publishing
    Compile / mainClass := Some("app.AppMain"),
    Compile / packageDoc / mappings := Seq(), //speed up build
    dockerExposedPorts := Seq(8080),
    dockerBaseImage := "openjdk:11-slim",
    dockerCommands := (dockerCommands.value match {
      case from :: rest =>
        List(
          from,
          //set JVM TTL, see https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-jvm-ttl.html
          Cmd("RUN", "mkdir", "-p", "$JAVA_HOME/jre/lib/security"),
          Cmd("RUN", "echo", "networkaddress.cache.ttl=60", ">>", "$JAVA_HOME/jre/lib/security/java.security")
        ) ++ rest
    })
  )
