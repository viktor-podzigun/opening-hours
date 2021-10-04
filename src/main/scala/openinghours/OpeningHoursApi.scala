package openinghours

import cats.effect.IO
import openinghours.OpeningHoursApi._
import openinghours.api._
import openinghours.model._

import java.time.LocalTime

class OpeningHoursApi(service: OpeningHoursService) {

  def formatOpeningHours(data: FormatReq): IO[String] = {
    service.formatOpeningHours(convertToFormatModel(data))
  }
}

object OpeningHoursApi {

  private val maxValue = LocalTime.MAX.toSecondOfDay

  private def convertToFormatModel(data: FormatReq): FormatModel = {
    data.map {
      case (dayRaw, itemsRaw) =>
        val day = OpeningHoursDay
          .fromString(dayRaw)
          .getOrElse(
            throw new IllegalArgumentException(
              s"""Unknown dayofweek: $dayRaw, expected one of: ${OpeningHoursDay.values
                .map(_.id)
                .mkString("[", ", ", "]")}"""
            )
          )
        val items = itemsRaw.map { itemRaw =>
          val value = itemRaw.value
          if (value < 0 || value > maxValue) {
            throw new IllegalArgumentException(s"Invalid value (valid values 0 - 86399): $value")
          }

          OpeningHoursItem(
            `type` = OpeningHoursType
              .fromString(itemRaw.`type`)
              .getOrElse(
                throw new IllegalArgumentException(
                  s"""Unknown type: ${itemRaw.`type`}, expected one of: ${OpeningHoursType.values
                    .map(_.id)
                    .mkString("[", ", ", "]")}"""
                )
              ),
            time = LocalTime.ofSecondOfDay(value)
          )

        }
        (day, items)
    }
  }
}
