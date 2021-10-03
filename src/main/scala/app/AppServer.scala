package app

import cats.data.{Kleisli, OptionT}
import cats.effect._
import org.http4s.{HttpApp, HttpRoutes, MediaType, Request, Response}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

class AppServer(
    port: Int,
    resources: AppResources,
    routes: AppRoutes
)(implicit cs: ContextShift[IO], timer: Timer[IO])
    extends Http4sDsl[IO] {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private val ec: ExecutionContext = ExecutionContext.fromExecutor(resources.httpExecutor)

  private val app: HttpApp[IO] =
    Router(
      "/" -> handleErrors(routes.service)
    ).orNotFound

  private val serverBuilder: BlazeServerBuilder[IO] =
    BlazeServerBuilder[IO](ec)
      .bindHttp(port, "0.0.0.0")
      .withHttpApp(app)
      .withConnectorPoolSize(20)

  def run(): IO[ExitCode] = serverBuilder.serve.compile.drain.as(ExitCode.Success)

  private def handleErrors(service: HttpRoutes[IO]): HttpRoutes[IO] =
    Kleisli { req: Request[IO] =>
      val errorHandler: Throwable => IO[Response[IO]] = {
        case ex: IllegalArgumentException =>
          BadRequest(Option(ex.getMessage).getOrElse(ex.toString))
        case ex =>
          for {
            _ <- IO(
              logger.error(
                s"InternalServerError has occurred, request.method = ${req.method.name}, request.pathInfo = ${req.pathInfo}",
                ex
              )
            )
            result <- InternalServerError(
              "InternalServerError has occurred, please, try again later",
              `Content-Type`(MediaType.text.plain)
            )
          } yield result
      }

      OptionT(service.run(req).value.handleErrorWith { ex =>
        errorHandler(ex).map(Option(_))
      })
    }
}
