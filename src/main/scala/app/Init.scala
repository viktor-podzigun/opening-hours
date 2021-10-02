package app

class Init(val resources: Resources) {

  lazy val routes = new HttpServerRoutes
}
