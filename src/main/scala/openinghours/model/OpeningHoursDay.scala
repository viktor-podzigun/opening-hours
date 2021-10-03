package openinghours.model

sealed trait OpeningHoursDay

object OpeningHoursDay {

  case object Monday extends OpeningHoursDay
  case object Tuesday extends OpeningHoursDay
  case object Wednesday extends OpeningHoursDay
  case object Thursday extends OpeningHoursDay
  case object Friday extends OpeningHoursDay
  case object Saturday extends OpeningHoursDay
  case object Sunday extends OpeningHoursDay
}
