package ziolayers.service

import zio.clock.Clock
import zio.console.Console
import zio.{ZIO, ZLayer}

object Logging {
  trait Service {
    def info(msg: String): ZIO[Any, Failure, Unit]
    def error(msg: String): ZIO[Any, Failure, Unit]
  }

  def info(msg: String): ZIO[Logging, Failure, Unit] = ZIO.accessM(_.get.info(msg))
  def error(msg: String): ZIO[Logging, Failure, Unit] = ZIO.accessM(_.get.error(msg))

  val console: ZLayer[Console, Nothing, Logging] = ZLayer.fromService(console =>
    new Service {
      private def log(msg: String, level: String) =
        console.putStrLn(s"$level: $msg").mapError(e => Failure(e.getMessage))
      def info(msg: String): ZIO[Any, Failure, Unit] = log(msg, "INFO")
      def error(msg: String): ZIO[Any, Failure, Unit] = log(msg, "ERROR")
    }
  )

  val chrono: ZLayer[Logging with Clock, Nothing, Logging] =
    ZLayer.fromServices[Logging.Service, Clock.Service, Logging.Service]((logging, clock) =>
      new Service {
        def info(msg: String): ZIO[Any, Failure, Unit] =
          clock.instant.map(i => logging.info(s"$i: $msg"))
        def error(msg: String): ZIO[Any, Failure, Unit] =
          clock.instant.map(i => logging.error(s"$i: $msg"))
      }
    )
}
