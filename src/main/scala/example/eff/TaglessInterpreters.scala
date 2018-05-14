package example.eff

import example.{Tweet, TweetsT, UserId, UsersT}
import org.atnos.eff.Eff
import org.atnos.eff.all.{_option, _throwableEither, fromOption, left, right}

final class UsersTEff[R: _option]() extends UsersT[Eff[R, ?]] {
  override def userId(email: String): Eff[R, UserId] =
    fromOption[R, UserId] {
      if (email == "hans@dampf.com") Some(UserId("hansdampf"))
      else None
    }
}

final class TweetsTEff[R: _throwableEither]() extends  TweetsT[Eff[R, ?]] {

  override def tweet(userId: UserId): Eff[R, Tweet] =
    if (userId.value == "hansdampf") right[R, Throwable, Tweet](Tweet("Juppie tweet"))
    else left[R, Throwable, Tweet](new Exception("User doesnt exist"))
}
