package openinghours.model

case class OpeningHoursType private (id: String, name: String) {

  override def toString: String = name
}

object OpeningHoursType {

  def fromString(id: String): Option[OpeningHoursType] = values.find(_.id == id)

  lazy val values: List[OpeningHoursType] = List(
    OpeningHoursType("open", "Open"),
    OpeningHoursType("close", "Close")
  )
}
