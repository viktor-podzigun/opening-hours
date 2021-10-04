package app

import cats.effect.IO
import org.http4s.HttpRoutes

trait ApiRoutes {

  def routes: HttpRoutes[IO]
}
