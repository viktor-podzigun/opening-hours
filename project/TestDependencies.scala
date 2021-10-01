import sbt._

object TestDependencies {

  val scalaTestVersion = "3.2.9"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
}
