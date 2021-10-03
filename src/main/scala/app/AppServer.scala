package app

import cats.effect._
import org.http4s.HttpApp
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

class AppServer(
    port: Int,
    resources: AppResources,
    routes: AppRoutes
)(implicit cs: ContextShift[IO], timer: Timer[IO])
    extends Http4sDsl[IO] {

  private val ec: ExecutionContext = ExecutionContext.fromExecutor(resources.httpExecutor)

  private val app: HttpApp[IO] =
    Router(
      "/" -> routes.service
    ).orNotFound

  private val serverBuilder: BlazeServerBuilder[IO] =
    BlazeServerBuilder[IO](ec)
      .bindHttp(port, "0.0.0.0")
      .withHttpApp(app)
      .withConnectorPoolSize(20)

  def run(): IO[ExitCode] = serverBuilder.serve.compile.drain.as(ExitCode.Success)
}
