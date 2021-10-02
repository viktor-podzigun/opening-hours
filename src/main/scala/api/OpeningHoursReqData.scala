package api

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class OpeningHoursReqData(`type`: String, value: Int)

object OpeningHoursReqData {

  implicit lazy val jsonConfig: Configuration = Configuration.default
  implicit lazy val jsonCodec: Codec[OpeningHoursReqData] =
    deriveConfiguredCodec[OpeningHoursReqData]: @annotation.nowarn("cat=lint-byname-implicit")
}
