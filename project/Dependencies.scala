import sbt._

object Dependencies {

  val logbackVersion = "1.2.5"
  val catsVersion = "2.5.0"
  val circeVersion = "0.13.0"
  val http4sVersion = "0.21.15"

  val swaggerUiVersion = "3.42.0"
  val swaggerVersion = "2.1.7"
  val swaggerScalaVersion = "2.5.2"
  val jakartaRsApiVersion = "2.1.6"

  lazy val logging = Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  lazy val cats = Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-laws" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsVersion
  )

  lazy val circe = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-refined" % circeVersion,
    "io.circe" %% "circe-shapes" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion
  )

  lazy val http4s = Seq(
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion
  )

  lazy val swagger = Seq(
    "org.webjars" % "swagger-ui" % swaggerUiVersion,
    "io.swagger.core.v3" % "swagger-annotations" % swaggerVersion,
    "io.swagger.core.v3" % "swagger-jaxrs2" % swaggerVersion,
    "jakarta.ws.rs" % "jakarta.ws.rs-api" % jakartaRsApiVersion,
    "com.github.swagger-akka-http" %% "swagger-scala-module" % swaggerScalaVersion
  )
}
