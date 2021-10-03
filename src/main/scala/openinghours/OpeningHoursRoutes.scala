package openinghours

import api._
import cats.effect.IO
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.{HttpRoutes, MediaType}

class OpeningHoursRoutes(api: OpeningHoursApi) {

  def routes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "openinghours" / "format" =>
        for {
          data <- req.as[OpeningHoursReq]
          result <- api.formatOpeningHours(data)
          resp <- Ok(result, `Content-Type`(MediaType.text.plain))
        } yield resp
    }
}
