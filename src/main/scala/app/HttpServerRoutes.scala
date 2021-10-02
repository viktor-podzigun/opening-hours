package app

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HttpServerRoutes extends Http4sDsl[IO] {

  def service: HttpRoutes[IO] =
    healthRoutes

  def healthRoutes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root            => Forbidden()
      case GET -> Root / "status" => Ok()
    }
}
