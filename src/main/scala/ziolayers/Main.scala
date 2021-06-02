package ziolayers

import zio.{ExitCode, ZEnv, ZIO}
import ziolayers.service.Logging

object Main extends zio.App {

  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    Logging.info("abc").provideCustomLayer(Logging.live).exitCode
}
