package openinghours

import app.ApiRoutes
import cats.effect.IO
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{Operation, tags}
import openinghours.api._
import org.http4s.EntityEncoder.stringEncoder
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

import javax.ws.rs

@tags.Tag(name = "opening-hours")
@rs.Path("/")
class OpeningHoursRoutes(api: OpeningHoursApi) extends ApiRoutes {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    formatOpeningHours
  }

  @rs.POST
  @rs.Path("/opening-hours/format")
  @Operation(
    summary = "Formats opening hours to human readable format",
    responses = Array(
      new ApiResponse(
        description = "Formatted output",
        content = Array(
          new Content(
            mediaType = "text/plain",
            schema = new Schema(implementation = classOf[String])
          )
        )
      )
    )
  )
  @RequestBody(
    required = true,
    content = Array(
      new Content(
        mediaType = "application/json",
        schema = new Schema(implementation = classOf[FormatReqDef])
      )
    )
  )
  def formatOpeningHours: PartialFunction[Request[IO], IO[Response[IO]]] = {
    case req @ POST -> Root / "opening-hours" / "format" =>
      for {
        data <- req.as[FormatReq]
        result <- api.formatOpeningHours(data)
        resp <- Ok(result)
      } yield resp
  }
}
