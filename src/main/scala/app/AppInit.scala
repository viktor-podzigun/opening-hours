package app

import openinghours._

class AppInit(val resources: AppResources) {

  lazy val openingHoursApi = new OpeningHoursApi
  lazy val openingHoursRoutes = new OpeningHoursRoutes(openingHoursApi)

  lazy val routes = new AppRoutes(
    List(
      openingHoursRoutes.routes
    )
  )
}
