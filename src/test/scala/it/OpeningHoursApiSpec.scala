package it

import openinghours.api._
import cats.effect.IO
import org.http4s.circe.CirceEntityCodec._
import org.http4s.{Method, Request, Status, Uri}
import org.scalatest.DoNotDiscover

@DoNotDiscover
class OpeningHoursApiSpec extends BaseIntegrationSpec {

  "/openinghours/format" should "return 400 if invalid dayofweek" in {
    //given
    val data = Map(
      "happyfriday" -> List(
        FormatReqData(
          `type` = "open",
          value = 32400
        )
      )
    )

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.BadRequest
    resp shouldBe {
      "Unknown dayofweek: happyfriday, expected one of: [monday, tuesday, wednesday, thursday, friday, saturday, sunday]"
    }
  }

  it should "return 400 if invalid type" in {
    //given
    val data = Map(
      "monday" -> List(
        FormatReqData(
          `type` = "happy",
          value = 32400
        )
      )
    )

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.BadRequest
    resp shouldBe {
      "Unknown type: happy, expected one of: [open, close]"
    }
  }

  it should "return 400 if value < 0" in {
    //given
    val data = Map(
      "monday" -> List(
        FormatReqData(
          `type` = "open",
          value = -1
        )
      )
    )

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.BadRequest
    resp shouldBe "Invalid value (valid values 0 - 86399): -1"
  }

  it should "return 400 if value > 86399" in {
    //given
    val data = Map(
      "monday" -> List(
        FormatReqData(
          `type` = "open",
          value = 86400
        )
      )
    )

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.BadRequest
    resp shouldBe "Invalid value (valid values 0 - 86399): 86400"
  }

  it should "return 200 and formatted opening hours" in {
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
