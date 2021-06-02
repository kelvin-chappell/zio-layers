package ziolayers.service

import zio.test.Assertion._
import zio.test._
import zio.test.environment._

object LoggingSpec extends DefaultRunnableSpec {
  def spec = suite("Logging")(
    testM("console") {
      for {
        _ <- Logging.info("abc")
        output <- TestConsole.output
      } yield {
        assert(output)(equalTo(Vector("abc")))
      }
    }.provideCustomLayer(Logging.console),
    testM("chrono") {
      for {
        _ <- Logging.info("abc")
        output <- TestConsole.output
      } yield {
        assert(output)(equalTo(Vector("abc")))
      }
    }.provideCustomLayer((TestClock.any ++ Logging.console) >>> Logging.chrono)
  )
}
