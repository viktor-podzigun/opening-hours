package openinghours

import cats.effect.IO
import openinghours.OpeningHoursService._
import openinghours.model._

import java.time.LocalTime
import java.time.format.DateTimeFormatter

class OpeningHoursService {

  def formatOpeningHours(model: FormatModel): IO[String] = {
    val flatModel = OpeningHoursDay.values.flatMap { day =>
      val items = model.getOrElse(day, Nil)
      if (items.isEmpty) List(day -> None)
      else {
        items.sortBy(_.time).map(i => (day, Some(i)))
      }
    }
    val normalizedModel = flatModel
      .foldLeft(List.empty[(OpeningHoursDay, List[OpeningHoursItem])]) {
        case (res, (day, None)) => (day, Nil) :: res
        case (res, (day, Some(item))) =>
          res match {
            case (lastDay, lastItems) :: tail if day == lastDay =>
              (lastDay, item :: lastItems) :: tail
            case (lastDay, lastItems) :: tail if item.`type` == OpeningHoursType.close =>
              (day, Nil) :: (lastDay, item :: lastItems) :: tail
            case _ =>
              (day, List(item)) :: res
          }
      }
      .reverse

    val results = normalizedModel.map {
      case (day, items) =>
        if (items.isEmpty) s"$day: Closed"
        else {
          val hours = items.reverse.foldLeft(new StringBuilder) {
            case (buff, item) =>
              if (item.`type` == OpeningHoursType.close) buff ++= " - "
              else if (buff.nonEmpty) buff ++= ", "

              buff ++= formatTime(item.time)
          }

          s"$day: $hours"
        }
    }
    IO(results.mkString("", "\n", "\n"))
  }
}

object OpeningHoursService {

  implicit val localTimeOrdering: Ordering[LocalTime] =
    (x: LocalTime, y: LocalTime) => x compareTo y

  private val hourFormatter = DateTimeFormatter.ofPattern("h a")
  private val hourAndMinuteFormatter = DateTimeFormatter.ofPattern("h:m a")

  private def formatTime(t: LocalTime): String = {
    if (t.getMinute == 0) t.format(hourFormatter)
    else t.format(hourAndMinuteFormatter)
  }
}
