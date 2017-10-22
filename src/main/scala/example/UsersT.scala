package example

import cats.{Monad, MonadError, ~>}
import example.KVStore._
import cats.implicits._

trait UsersT[M[_]] {

  def userId(email: String): M[UserId]

}

class UsersInterpreter[M[_] : Monad : MonadError[?[_], String]](interpreter: KVStoreA ~> M) extends UsersT[M] {

  override def userId(email: String): M[UserId] =
    get[UserId](email)
      .foldMap(interpreter)
      .flatMap {
        case Some(userId) =>
          Monad[M].pure(userId)
        case None =>
          MonadError[M, String].raiseError(s"UserId for $email not found")
      }

}
