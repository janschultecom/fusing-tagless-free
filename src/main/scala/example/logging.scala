package example

import cats._
import cats.free.Free._
import cats.free._
import cats.implicits._

sealed trait Log[A]

case class Info(msg: String) extends Log[Unit]

case class Error(msg: String) extends Log[Unit]


object Logging {


  type LogF[A] = Free[Log, A]

  def info(msg: String): LogF[Unit] =
    liftF[Log, Unit](Info(msg))

  def error(msg: String): LogF[Unit] =
    liftF[Log, Unit](Error(msg))

  class LogI[F[_]](implicit I: InjectK[Log, F]) {
    def infoI(msg: String): Free[F, Unit] = Free.inject[Log, F](Info(msg))

    def errorI(msg: String): Free[F, Unit] = Free.inject[Log, F](Error(msg))
  }

  implicit def logI[F[_]](implicit I: InjectK[Log, F]): LogI[F] = new LogI[F]

}