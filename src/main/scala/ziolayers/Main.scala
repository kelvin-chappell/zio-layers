package ziolayers

import zio.{ExitCode, ZEnv, ZIO}
import ziolayers.service.Logging
import zio._
import zio.clock.Clock
import zio.console.Console
import ziolayers.service.Logging.info

object Main extends zio.App {

  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
    val layer: ZLayer[Console with Clock, Nothing, Logging] =
      (Clock.live ++ Logging.console) >>> Logging.chrono
    Logging.info("abc").provideCustomLayer(layer).exitCode
  }
}
