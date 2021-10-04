package openinghours.api

import io.swagger.v3.oas.annotations.media.Schema

case class FormatReqDef(
    @Schema(required = false) monday: List[FormatReqData],
    @Schema(required = false) tuesday: List[FormatReqData],
    @Schema(required = false) wednesday: List[FormatReqData],
    @Schema(required = false) thursday: List[FormatReqData],
    @Schema(required = false) friday: List[FormatReqData],
    @Schema(required = false) saturday: List[FormatReqData],
    @Schema(required = false) sunday: List[FormatReqData]
)
