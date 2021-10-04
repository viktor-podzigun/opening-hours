package openinghours.model

case class OpeningHoursType private (id: String) {

  override def toString: String = id
}

object OpeningHoursType {

  def fromString(id: String): Option[OpeningHoursType] = values.find(_.id == id)

  private var _values: List[OpeningHoursType] = Nil
  private def register(t: OpeningHoursType): OpeningHoursType = {
    _values = t :: _values
    t
  }

  lazy val values: List[OpeningHoursType] = _values

  val open: OpeningHoursType = register(OpeningHoursType("open"))
  val close: OpeningHoursType = register(OpeningHoursType("close"))
}
