package app

import cats.effect.{ExitCode, IO, IOApp, Resource}

class AppMain(port: Int) {
  self: IOApp =>

  override def run(args: List[String]): IO[ExitCode] = {
    val initResource = for {
      resources <- AppResources.create()
      init <- initialise(resources)
    } yield init

    initResource.use { init =>
      // start application
      new AppServer(port, init.resources, init.routes).run()
    }
  }

  protected def initialise(resources: AppResources): Resource[IO, AppInit] = {
    Resource.eval(IO.delay(new AppInit(resources)))
  }
}

object AppMain extends AppMain(port = 8080) with IOApp
