package example

import cats.~>
import example.technical._
import cats.implicits._

object Main extends App {

  def interpreter(counterName:String) : KVStoreA ~> ErrorOr = loggingCompiler andThen metricsCompiler(counterName) andThen kvStoreCompiler

  val usersInterpreter: UsersInterpreter[ErrorOr] = new UsersInterpreter(interpreter("users"))
  val tweetsInterpreter: TweetsInterpreter[ErrorOr] = new TweetsInterpreter(interpreter("tweets"))

  val program: String => ErrorOr[Tweet] = Program.program(usersInterpreter,tweetsInterpreter)

  println(program("hans@dampf.com"))
  println(program("franz@dampf.com"))
  println(program("fritz@dampf.com"))

}
