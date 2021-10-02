package it

import app.{Init, Resources}
import cats.effect.IO
import org.http4s.client.Client

class TestInit(resources: Resources, val port: Int, val httpClient: Client[IO]) extends Init(resources) {

  //mock some dependencies here
}
