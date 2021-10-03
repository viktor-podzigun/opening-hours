package openinghours.api

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class FormatReqData(`type`: String, value: Int)

object FormatReqData {

  implicit lazy val jsonConfig: Configuration = Configuration.default
  implicit lazy val jsonCodec: Codec[FormatReqData] =
    deriveConfiguredCodec[FormatReqData]: @annotation.nowarn("cat=lint-byname-implicit")
}
