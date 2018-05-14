package example.free

import cats.InjectK
import cats.free.Free
import example.{Tweet, TweetsT, UserId, UsersT}

final class UsersTFree[F[_]](implicit I: InjectK[Option, F]) extends UsersT[Free[F, ?]] {
  override def userId(email: String): Free[F, UserId] =
    Free.inject[Option, F] {
      if (email == "hans@dampf.com") Some(UserId("hansdampf"))
      else None
    }
}

object UsersTFree {
  implicit def usersTOptions[F[_]](implicit I: InjectK[Option, F]): UsersTFree[F] =
    new UsersTFree[F]
}

final class TweetsTFree[F[_]](implicit I: InjectK[Either[Throwable, ?], F]) extends TweetsT[Free[F, ?]] {
  override def tweet(userId: UserId): Free[F, Tweet] =
    Free.inject[Either[Throwable, ?], F] {
      if (userId.value == "hansdampf") Right(Tweet("Hello tweet"))
      else Left(new Error(s"User id ${userId.value} not found"))
    }
}

object TweetsTFree {
  implicit def tweetsTEither[F[_]](implicit I: InjectK[Either[Throwable, ?], F]): TweetsTFree[F] = new TweetsTFree[F]
}

