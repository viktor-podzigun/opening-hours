package it.util.injector

/**
  * Inspired by `org.scalatestplus.play.ServerProvider`
  */
trait TestInjectorProvider[T] {

  implicit def injector: T
}

object TestInjectorProvider {

  private[injector] val injectorArg = "it.test.injector"
}
