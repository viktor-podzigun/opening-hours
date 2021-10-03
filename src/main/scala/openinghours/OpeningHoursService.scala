package openinghours

import cats.effect.IO
import openinghours.model._

class OpeningHoursService {

  def formatOpeningHours(model: FormatModel): IO[String] = {
    IO(model.toString())
  }
}
