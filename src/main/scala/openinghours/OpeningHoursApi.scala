package openinghours

import cats.effect.IO
import openinghours.api._

class OpeningHoursApi {

  def formatOpeningHours(data: FormatReq): IO[String] = {
    import io.circe.syntax._
    IO(data.asJson.spaces2)
  }
}
