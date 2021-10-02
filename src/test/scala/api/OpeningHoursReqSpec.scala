package api

import io.circe.parser._
import io.circe.syntax._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OpeningHoursReqSpec extends AnyFlatSpec with Matchers {

  private val data: OpeningHoursReq = Map(
    "monday" -> List(
      OpeningHoursItem(
        `type` = "open",
        value = 32400
      ),
      OpeningHoursItem(
        `type` = "close",
        value = 72000
      )
    )
  )

  private val expectedJson =
    """{
      |  "monday" : [
      |    {
      |      "type" : "open",
      |      "value" : 32400
      |    },
      |    {
      |      "type" : "close",
      |      "value" : 72000
      |    }
      |  ]
      |}""".stripMargin

  it should "deserialize data from json" in {
    //when
    val result = decode[OpeningHoursReq](expectedJson)

    //then
    result shouldBe Right(data)
  }

  it should "serialize data to json" in {
    //when
    val result = data.asJson

    //then
    result.spaces2 shouldBe expectedJson
  }
}
