package it

import openinghours.api._
import cats.effect.IO
import org.http4s.circe.CirceEntityCodec._
import org.http4s.{Method, Request, Status, Uri}
import org.scalatest.DoNotDiscover

@DoNotDiscover
class OpeningHoursApiSpec extends BaseIntegrationSpec {

  "/openinghours/format" should "return formatted opening hours" in {
    //given
    val data = Map(
      "monday" -> List(
        FormatReqData(
          `type` = "open",
          value = 32400
        ),
        FormatReqData(
          `type` = "close",
          value = 72000
        )
      )
    )

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.Ok
    resp should not be empty
  }

  private def callOpeningHoursFormat(data: FormatReq): (Status, String) = {
    val req = Request[IO](
      method = Method.POST,
      uri = Uri.unsafeFromString(s"$baseUrl/openinghours/format")
    ).withEntity(data)

    val resp = apiClient.run(req).use(IO(_)).unsafeRunSync()
    (resp.status, resp.bodyText.compile.string.unsafeRunSync())
  }
}
