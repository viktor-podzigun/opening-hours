package it

import app.{AppInit, AppResources}
import cats.effect.{ContextShift, IO}
import org.http4s.client.Client

class TestInit(resources: AppResources, val port: Int, val httpClient: Client[IO])(implicit cs: ContextShift[IO])
    extends AppInit(resources) {

  //mock some dependencies here
}
