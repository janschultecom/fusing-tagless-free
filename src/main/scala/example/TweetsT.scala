package example

import cats.{Monad, MonadError, ~>}
import example.KVStore._
import cats.implicits._

trait TweetsT[M[_]] {

  def tweet(userId: UserId): M[Tweet]

}

class TweetsInterpreter[M[_] : Monad : MonadError[?[_], String]](interpreter: KVStoreA ~> M) extends TweetsT[M] {

  override def tweet(userId: UserId): M[Tweet] =
    get[Tweet](userId.value)
      .foldMap(interpreter)
      .flatMap {
        case Some(tweet) =>
          Monad[M].pure(tweet)
        case None =>
          MonadError[M, String].raiseError(s"Tweet for $userId not found")
      }

}
