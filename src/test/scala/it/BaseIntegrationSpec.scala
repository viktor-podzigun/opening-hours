package it

import cats.effect.IO
import org.http4s.client.Client
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.should.Matchers
import org.scalatest.time._
import org.scalatest.{BeforeAndAfterEach, Inside, Suite}
import util.injector.ConfiguredTestInjector

trait BaseIntegrationSpec
    extends Suite
    with ConfiguredTestInjector[TestInit]
    with Matchers
    with Inside
    with Eventually
    with BeforeAndAfterEach {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(5, Seconds)),
    interval = scaled(Span(10, Millis))
  )

  protected lazy val apiClient: Client[IO] = injector.httpClient
  protected lazy val baseUrl = s"http://localhost:${injector.port}"

  override protected def beforeEach(): Unit = {}

  override protected def afterEach(): Unit = {}
}
