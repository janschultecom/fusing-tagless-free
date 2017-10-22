package example

import cats.Monad
import cats.implicits._

object Program {

  def program[M[_] : Monad](usersT: UsersT[M],tweetsT: TweetsT[M])(email:String):M[Tweet] =
    for {
      userId <- usersT.userId(email)
      tweet <- tweetsT.tweet(userId)
    } yield tweet

}
