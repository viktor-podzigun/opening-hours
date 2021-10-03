package app

import openinghours._

class Init(val resources: Resources) {

  lazy val openingHoursApi = new OpeningHoursApi
  lazy val openingHoursRoutes = new OpeningHoursRoutes(openingHoursApi)

  lazy val routes = new HttpServerRoutes(
    List(
      openingHoursRoutes.routes
    )
  )
}
