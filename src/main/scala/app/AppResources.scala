package app

import cats.effect.{ContextShift, IO, Resource}

import java.util.concurrent.{ExecutorService, Executors}

case class AppResources(httpExecutor: ExecutorService)

object AppResources {

  private val threadPoolSize = math.max(8, Runtime.getRuntime.availableProcessors())

  def create()(implicit cs: ContextShift[IO]): Resource[IO, AppResources] = {
    for {
      httpExecutor <- Resource.make(IO.delay(Executors.newFixedThreadPool(threadPoolSize))) { exec =>
        IO.delay(exec.shutdown())
      }
    } yield {
      AppResources(httpExecutor)
    }
  }
}
