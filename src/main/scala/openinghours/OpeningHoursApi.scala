package openinghours

import api._
import cats.effect.IO

class OpeningHoursApi {

  def formatOpeningHours(data: OpeningHoursReq): IO[String] = {
    import io.circe.syntax._
    IO(data.asJson.spaces2)
  }
}
