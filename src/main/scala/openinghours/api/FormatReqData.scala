package openinghours.api

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import io.swagger.v3.oas.annotations.media.Schema

case class FormatReqData(
    @Schema(required = true, allowableValues = Array("open", "close")) `type`: String,
    @Schema(required = true, minimum = "0", maximum = "86399") value: Int
)

object FormatReqData {

  implicit lazy val jsonConfig: Configuration = Configuration.default
  implicit lazy val jsonCodec: Codec[FormatReqData] =
    deriveConfiguredCodec[FormatReqData]: @annotation.nowarn("cat=lint-byname-implicit")
}
