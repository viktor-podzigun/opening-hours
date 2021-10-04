package it

import app.ApiRoutes
import cats.effect.IO
import it.util.injector.ConfiguredTestInjector
import org.http4s.client.Client
import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class BaseIntegrationSpec extends AnyFlatSpec with ConfiguredTestInjector[TestInit] with Matchers with Inside {

  protected lazy val apiClient: Client[IO] = injector.httpClient
  protected lazy val baseUrl = s"http://localhost:${injector.port}"
  protected lazy val apiRoutes: List[ApiRoutes] = injector.apiRoutes
}
