package it

import cats.effect.IO
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.{Request, Status, Uri}
import org.scalatest.DoNotDiscover

@DoNotDiscover
class ServerApiSpec extends BaseIntegrationSpec {

  "/" should "return Forbidden status" in {
    //given
    val uri = Uri.unsafeFromString(s"$baseUrl")
    val req = Request[IO](uri = uri)

    //when
    val resp = apiClient.run(req).use(r => IO(r)).unsafeRunSync()

    //then
    resp.status shouldBe Status.Forbidden
    resp.bodyText.compile.string.unsafeRunSync() shouldBe ""
  }

  "/status" should "return correct status and body" in {
    //given
    val uri = Uri.unsafeFromString(s"$baseUrl/status")
    val req = Request[IO](uri = uri)

    //when
    val resp = apiClient.run(req).use(r => IO(r)).unsafeRunSync()

    //then
    resp.status shouldBe Status.Ok
    resp.bodyText.compile.string.unsafeRunSync() shouldBe ""
  }

  "/swagger.html" should "redirect to swagger ui html page in webjar" in {
    //given
    val uri = Uri.unsafeFromString(s"$baseUrl/swagger.html")
    val req = Request[IO](uri = uri)

    //when
    val resp = apiClient.run(req).use(r => IO(r)).unsafeRunSync()

    //then
    resp.status shouldBe Status.SeeOther
    resp.headers.get(Location).get.uri shouldBe {
      uri"/swagger-ui/3.42.0/index.html?url=/api-docs"
    }
  }

  "/swagger-ui/3.42.0/index.html" should "return html page from webjar" in {
    //given
    val uri = Uri.unsafeFromString(s"$baseUrl/swagger-ui/3.42.0/index.html")
    val req = Request[IO](uri = uri)

    //when
    val resp = apiClient.run(req).use(r => IO(r)).unsafeRunSync()

    //then
    resp.status shouldBe Status.Ok
    resp.bodyText.compile.string.unsafeRunSync() should include("<title>Swagger UI</title>")
  }

  "/api-docs" should "return generated openapi.yaml file" in {
    //given
    val uri = Uri.unsafeFromString(s"$baseUrl/api-docs")
    val req = Request[IO](uri = uri)

    //when
    val resp = apiClient.run(req).use(r => IO(r)).unsafeRunSync()

    //then
    resp.status shouldBe Status.Ok
    resp.bodyText.compile.string.unsafeRunSync() should include("title: Opening Hours REST API")
  }
}
