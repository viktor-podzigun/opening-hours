package it

import cats.effect.IO
import org.http4s.{Request, Status, Uri}
import org.scalatest.DoNotDiscover
import org.scalatest.flatspec.AnyFlatSpec

@DoNotDiscover
class HealthPathTest extends AnyFlatSpec with BaseIntegrationSpec {

  "/" should "return Forbidden status" in {
    //given
    val uri = Uri.unsafeFromString(s"$baseUrl")
    val req = Request[IO](uri = uri)

    //when
    val resp = apiClient.run(req).use(r => IO(r)).unsafeRunSync()

    //then
    resp.status shouldBe Status.Forbidden
    resp.as[String].unsafeRunSync() shouldBe ""
  }

  "/status" should "return correct status and body" in {
    //given
    val uri = Uri.unsafeFromString(s"$baseUrl/status")
    val req = Request[IO](uri = uri)

    //when
    val resp = apiClient.run(req).use(r => IO(r)).unsafeRunSync()

    //then
    resp.status shouldBe Status.Ok
    resp.as[String].unsafeRunSync() shouldBe ""
  }
}
