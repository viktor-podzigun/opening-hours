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
      "Unknown type: happy, expected one of: [close, open]"
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

  it should "return 200 and closed all week if empty request data" in {
    //given
    val data = Map.empty[String, List[FormatReqData]]

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.Ok
    resp shouldBe {
      """Monday: Closed
        |Tuesday: Closed
        |Wednesday: Closed
        |Thursday: Closed
        |Friday: Closed
        |Saturday: Closed
        |Sunday: Closed
        |""".stripMargin
    }
  }

  it should "return 200 and formatted output for corner cases" in {
    //given
    val data = Map(
      "saturday" -> List(
        FormatReqData(`type` = "open", value = 32400),
        FormatReqData(`type` = "close", value = 3600),
        FormatReqData(`type` = "close", value = 39600),
        FormatReqData(`type` = "close", value = 82800),
        FormatReqData(`type` = "open", value = 57600)
      ),
      "friday" -> List(
        FormatReqData(`type` = "open", value = 64800)
      )
    )

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.Ok
    resp shouldBe {
      """Monday: Closed
        |Tuesday: Closed
        |Wednesday: Closed
        |Thursday: Closed
        |Friday: 6 PM - 1 AM
        |Saturday: 9 AM - 11 AM, 4 PM - 11 PM
        |Sunday: Closed
        |""".stripMargin
    }
  }

  it should "return 200 and formatted output for example JSON" in {
    //given
    val data = Map(
      "monday" -> Nil,
      "tuesday" -> List(
        FormatReqData(`type` = "open", value = 36000),
        FormatReqData(`type` = "close", value = 64800)
      ),
      "wednesday" -> Nil,
      "thursday" -> List(
        FormatReqData(`type` = "open", value = 37800),
        FormatReqData(`type` = "close", value = 64800)
      ),
      "friday" -> List(
        FormatReqData(`type` = "open", value = 36000)
      ),
      "saturday" -> List(
        FormatReqData(`type` = "close", value = 3600),
        FormatReqData(`type` = "open", value = 36000)
      ),
      "sunday" -> List(
        FormatReqData(`type` = "close", value = 3600),
        FormatReqData(`type` = "open", value = 43200),
        FormatReqData(`type` = "close", value = 75600)
      )
    )

    //when
    val (status, resp) = callOpeningHoursFormat(data)

    //then
    status shouldBe Status.Ok
    resp shouldBe {
      """Monday: Closed
        |Tuesday: 10 AM - 6 PM
        |Wednesday: Closed
        |Thursday: 10:30 AM - 6 PM
        |Friday: 10 AM - 1 AM
        |Saturday: 10 AM - 1 AM
        |Sunday: 12 PM - 9 PM
        |""".stripMargin
    }
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
