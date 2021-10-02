package it.util.injector

import it.util.injector.TestInjectorProvider.injectorArg
import org.scalatest.{Args, Status, Suite, SuiteMixin}

/**
  * Inspired by `org.scalatestplus.play.ConfiguredServer`
  */
trait ConfiguredTestInjector[T] extends SuiteMixin with TestInjectorProvider[T] { this: Suite =>

  private var configuredInjector: T = _

  /**
    * The "configured" `Injector` instance that was passed into `run` via the `ConfigMap`.
    *
    * @return the configured `Injector`
    */
  implicit final def injector: T = synchronized { configuredInjector }

  abstract override def run(testName: Option[String], args: Args): Status = {
    args.configMap.getOptional[Any](injectorArg) match {
      case Some(c) => synchronized { configuredInjector = c.asInstanceOf[T] }
      case None =>
        throw new IllegalStateException(
          s"Trait ConfiguredTestInjector needs an injector value associated with key '$injectorArg'" +
            " in the config map. Did you forget to annotate a nested suite with @DoNotDiscover?"
        )
    }

    super.run(testName, args)
  }
}
