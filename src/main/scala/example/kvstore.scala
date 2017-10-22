package example

import cats.InjectK
import cats.free.Free
import cats.free.Free.liftF


sealed trait KVStoreA[A]

case class Put[T](key: String, value: T) extends KVStoreA[Unit]

case class Get[T](key: String) extends KVStoreA[Option[T]]

object KVStore {


  type KVStoreF[A] = Free[KVStoreA, A]

  // Put returns nothing (i.e. Unit).
  def put[T](key: String, value: T): KVStoreF[Unit] =
    liftF[KVStoreA, Unit](Put[T](key, value))

  // Get returns a T value.
  def get[T](key: String): KVStoreF[Option[T]] =
    liftF[KVStoreA, Option[T]](Get[T](key))


  class KVStoreI[F[_]](implicit I: InjectK[KVStoreA, F]) {
    def putI[T](key: String, value: T): Free[F, Unit] = Free.inject[KVStoreA, F](Put(key, value))

    def getI[T](key: String): Free[F, Option[T]] = Free.inject[KVStoreA, F](Get[T](key))
  }

  implicit def kvStoreI[F[_]](implicit I: InjectK[KVStoreA, F]): KVStoreI[F] = new KVStoreI[F]

}
