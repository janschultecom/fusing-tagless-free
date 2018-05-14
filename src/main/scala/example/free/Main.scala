package example.free

import cats.arrow.FunctionK
import cats.data.EitherK
import cats.free.Free
import cats.instances.either._
import cats.{Monad, ~>}
import example.Program.{program => tweetByMail}
import example.{Tweet, TweetsT, UsersT}

object Main extends App {

  type UsersTweetsApp[A] = EitherK[Option, Either[Throwable, ?], A]

  type AppAlg[A] = Free[UsersTweetsApp, A]

  val userAlg: UsersT[AppAlg] = new UsersTFree[UsersTweetsApp]()
  val tweetsAlg: TweetsT[AppAlg] = new TweetsTFree[UsersTweetsApp]()

  type ErrorOr[A] = Either[Throwable, A]

  val optionInterpreter = new (Option ~> ErrorOr) {
    override def apply[A](fa: Option[A]): ErrorOr[A] = fa.toRight(new Throwable(""))
  }

  val interpreter2: UsersTweetsApp ~> ErrorOr = optionInterpreter or FunctionK.id[ErrorOr]

  private val M: Monad[ErrorOr] = catsStdInstancesForEither[Throwable]
  val result: ErrorOr[Tweet] = tweetByMail[AppAlg](userAlg, tweetsAlg)("hans@dampf.com")
    .foldMap[ErrorOr](interpreter2)(M)

  println(result)

}
