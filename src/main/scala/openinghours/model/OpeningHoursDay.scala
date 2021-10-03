package openinghours.model

case class OpeningHoursDay private (id: String, name: String) {

  override def toString: String = name
}

object OpeningHoursDay {

  def fromString(id: String): Option[OpeningHoursDay] = values.find(_.id == id)

  lazy val values: List[OpeningHoursDay] = List(
    OpeningHoursDay("monday", "Monday"),
    OpeningHoursDay("tuesday", "Tuesday"),
    OpeningHoursDay("wednesday", "Wednesday"),
    OpeningHoursDay("thursday", "Thursday"),
    OpeningHoursDay("friday", "Friday"),
    OpeningHoursDay("saturday", "Saturday"),
    OpeningHoursDay("sunday", "Sunday")
  )
}
