package app

import cats.effect.{Blocker, ContextShift, IO}
import openinghours._

class AppInit(val resources: AppResources)(implicit cs: ContextShift[IO]) {

  lazy val openingHoursService = new OpeningHoursService
  lazy val openingHoursApi = new OpeningHoursApi(openingHoursService)
  lazy val openingHoursRoutes = new OpeningHoursRoutes(openingHoursApi)

  lazy val apiRoutes: List[ApiRoutes] = List(
    openingHoursRoutes
  )
  lazy val routes = new AppRoutes(
    blocker = Blocker.liftExecutorService(resources.blockingPool),
    apiRoutes = apiRoutes
  )
}
