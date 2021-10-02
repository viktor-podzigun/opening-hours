package it.util.injector

import org.scalatest.{Args, Status, Suite, SuiteMixin}

/**
  * Inspired by `org.scalatestplus.play.OneServerPerSuite`
  */
trait OneTestInjectorPerSuite[T] extends SuiteMixin with TestInjectorProvider[T] { this: Suite =>

  abstract override def run(testName: Option[String], args: Args): Status = {
    val newConfigMap = args.configMap + (TestInjectorProvider.injectorArg -> injector)
    val newArgs = args.copy(configMap = newConfigMap)
    super.run(testName, newArgs)
  }
}
