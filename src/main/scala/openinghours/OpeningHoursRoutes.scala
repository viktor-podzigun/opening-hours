package openinghours

import cats.effect.IO
import openinghours.api._
import org.http4s.EntityEncoder.stringEncoder
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.io._

class OpeningHoursRoutes(api: OpeningHoursApi) {

  def routes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "openinghours" / "format" =>
        for {
          data <- req.as[FormatReq]
          result <- api.formatOpeningHours(data)
          resp <- Ok(result)
        } yield resp
    }
  }
}
