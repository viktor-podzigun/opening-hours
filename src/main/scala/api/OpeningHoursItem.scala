package api

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class OpeningHoursItem(`type`: String, value: Int)

object OpeningHoursItem {

  implicit lazy val jsonConfig: Configuration = Configuration.default
  implicit lazy val jsonCodec: Codec[OpeningHoursItem] =
    deriveConfiguredCodec[OpeningHoursItem]: @annotation.nowarn("cat=lint-byname-implicit")
}
