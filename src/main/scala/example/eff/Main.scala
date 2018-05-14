package example.eff

import org.atnos.eff._
import all._
import cats.{Traverse, ~>}
import syntax.all._
import org.atnos.eff.{Eff, Fx2}
import example.Program.{program => tweetByMail}
import example.Tweet

object Main extends App {

  type EffectStack = Fx2[Option, Either[Throwable, ?]]

  val userAlg = new UsersTEff[EffectStack]
  val tweetsAlg = new TweetsTEff[EffectStack]

  val program: String => Eff[EffectStack, Tweet] =
    tweetByMail(userAlg, tweetsAlg)

  private val value = program("hans@dampf.com")

  val result : Either[Throwable, Option[Tweet]] =
    value.runOption.runEither.run
  println(result) // Right(Some(Tweet(Juppie tweet)))


}
