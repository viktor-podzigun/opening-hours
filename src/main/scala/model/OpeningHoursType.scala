package model

sealed trait OpeningHoursType

object OpeningHoursType {

  case object Open extends OpeningHoursType
  case object Close extends OpeningHoursType
}
