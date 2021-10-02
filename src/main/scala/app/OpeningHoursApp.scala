package app

import cats.effect.{ExitCode, IO, IOApp, Resource}

class OpeningHoursApp(port: Int) {
  self: IOApp =>

  override def run(args: List[String]): IO[ExitCode] = {
    val initResource = for {
      resources <- Resources.create()
      init <- initialise(resources)
    } yield init

    initResource.use { init =>
      // start application
      new HttpServer(port, init.resources, init.routes).run()
    }
  }

  protected def initialise(resources: Resources): Resource[IO, Init] = {
    Resource.eval(IO.delay(new Init(resources)))
  }
}

object OpeningHoursApp extends OpeningHoursApp(port = 8080) with IOApp
