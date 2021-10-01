import sbt._

object Dependencies {

  val logbackVersion = "1.2.5"
  val catsVersion = "2.4.2"
  val catsEffectVersion = "2.3.3"
  val circeVersion = "0.13.0"
  val http4sVersion = "0.21.15"

  lazy val logging = Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  lazy val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-laws" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion
  )

  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-refined" % circeVersion,
    "io.circe" %% "circe-shapes" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion
  )

  lazy val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion
  )
}
