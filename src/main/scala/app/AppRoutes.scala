package app

import cats.effect.{Blocker, ContextShift, IO}
import cats.implicits._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.server.staticcontent.{WebjarService, webjarService}
import org.http4s.{HttpRoutes, Response, StaticFile, Status}

class AppRoutes(blocker: Blocker, apiRoutes: List[ApiRoutes])(implicit cs: ContextShift[IO]) extends Http4sDsl[IO] {

  def service: HttpRoutes[IO] =
    (
      healthRoutes ::
        webjars ::
        swaggerRoutes ::
        apiRoutes.map(_.routes)
    ).reduce(_ <+> _)

  private def healthRoutes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root            => Forbidden()
      case GET -> Root / "status" => Ok()
    }

  private def webjars: HttpRoutes[IO] =
    webjarService(WebjarService.Config(blocker = blocker))

  private def swaggerRoutes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "swagger.html" =>
        IO(
          Response[IO]()
            .withStatus(Status.SeeOther)
            .withHeaders(Location(uri"/swagger-ui/3.42.0/index.html?url=/api-docs"))
        )
      case request @ GET -> Root / "api-docs" =>
        StaticFile
          .fromResource("/openapi.yaml", blocker, Some(request))
          .getOrElseF(NotFound())
    }
}
