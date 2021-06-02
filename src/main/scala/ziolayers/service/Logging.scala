package ziolayers.service

import zio.console.Console
import zio.{ZIO, ZLayer}
import ziolayers.Failure

object Logging {
  trait Service {
    def info(msg: String): ZIO[Any, Failure, Unit]
    def error(msg: String): ZIO[Any, Failure, Unit]
  }

  def info(msg: String): ZIO[Logging, Failure, Unit] = ZIO.accessM(_.get.info(msg))
  def error(msg: String): ZIO[Logging, Failure, Unit] = ZIO.accessM(_.get.error(msg))

  val live: ZLayer[Console, Nothing, Logging] = ZLayer.fromService(console =>
    new Service {
      private def log(msg: String, level: String) =
        console.putStrLn(s"$level: $msg").mapError(e => Failure(e.getMessage))
      def info(msg: String): ZIO[Any, Failure, Unit] = log(msg, "INFO")
      def error(msg: String): ZIO[Any, Failure, Unit] = log(msg, "ERROR")
    }
  )
}
