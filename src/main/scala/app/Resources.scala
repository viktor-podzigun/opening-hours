package app

import cats.effect.{ContextShift, IO, Resource}

import java.util.concurrent.{ExecutorService, Executors}

case class Resources(httpExecutor: ExecutorService)

object Resources {

  private val threadPoolSize = math.max(8, Runtime.getRuntime.availableProcessors())

  def create()(implicit cs: ContextShift[IO]): Resource[IO, Resources] = {
    for {
      httpExecutor <- Resource.make(IO.delay(Executors.newFixedThreadPool(threadPoolSize))) { exec =>
        IO.delay(exec.shutdown())
      }
    } yield {
      Resources(httpExecutor)
    }
  }
}
