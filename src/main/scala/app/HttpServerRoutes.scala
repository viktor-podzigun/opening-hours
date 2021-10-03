package app

import cats.effect.IO
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HttpServerRoutes(otherRoutes: List[HttpRoutes[IO]]) extends Http4sDsl[IO] {

  def service: HttpRoutes[IO] =
    otherRoutes.foldLeft(healthRoutes) { (res, routes) =>
      res <+> routes
    }

  def healthRoutes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root            => Forbidden()
      case GET -> Root / "status" => Ok()
    }
}
