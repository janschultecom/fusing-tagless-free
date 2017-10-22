import cats.data.EitherK

package object example {

  type ErrorOr[A] = Either[String,A]
  type LoggingKVStore[A] = EitherK[KVStoreA,Log,A]
}
