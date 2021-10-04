package it

import app.{AppInit, AppMain, AppResources}
import cats.effect._
import it.util.HttpPortUtil
import it.util.injector.OneTestInjectorPerSuite
import org.http4s.client.blaze.BlazeClientBuilder
import org.scalatest.{BeforeAndAfterAll, Suites}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

class AllAppIntegrationTests
    extends Suites(
      new SwaggerTest,
      new ServerApiSpec,
      new OpeningHoursApiSpec
    )
    with OneTestInjectorPerSuite[TestInit]
    with BeforeAndAfterAll {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private val httpPort: Int = HttpPortUtil.findFreePort
  private var shutdown: Option[CancelToken[IO]] = None

  private var _init: TestInit = _
  implicit lazy val injector: TestInit = _init

  private lazy val app: AppMain = new AppMain(httpPort) with IOApp {

    override implicit def contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    override implicit def timer: Timer[IO] = IO.timer(ExecutionContext.global)

    override def initialise(resources: AppResources): Resource[IO, AppInit] = {
      for {
        client <- BlazeClientBuilder[IO](ExecutionContext.global).resource
      } yield {
        _init = new TestInit(resources, httpPort, client)
        _init
      }
    }
  }

  override def beforeAll(): Unit = {
    super.beforeAll()

    logger.info("Starting Http Server...")
    val start: SyncIO[CancelToken[IO]] = app.run(Nil).runCancelable {
      case Left(e)  => IO(logger.error("Server error", e))
      case Right(_) => IO.unit
    }
    shutdown = Some(start.unsafeRunSync())
  }

  override def afterAll(): Unit = {
    logger.info("Stopping Server...")
    shutdown.foreach(_.unsafeRunSync())
    logger.info("Server Stopped")

    super.afterAll()
  }
}
