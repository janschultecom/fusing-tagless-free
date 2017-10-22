package example

import cats.implicits._
import cats.~>

import scala.collection.mutable


object technical {

  val kvStoreCompiler: KVStoreA ~> ErrorOr = new (KVStoreA ~> ErrorOr) {

    val kvs = mutable.Map[String, Any](
      "hans@dampf.com" -> UserId("hans"),
      "hans" -> Tweet("free combined with tagless"),
      "franz@dampf.com" -> UserId("franz")
    )

    def apply[A](fa: KVStoreA[A]): ErrorOr[A] =
      fa match {
        case Put(key, value) =>
          kvs(key) = value
          ().asInstanceOf[A].asRight[String]
        case Get(key) =>
          kvs
            .get(key)
            .asInstanceOf[A].asRight[String]
      }
  }

  val loggingCompiler: KVStoreA ~> KVStoreA = new (KVStoreA ~> KVStoreA) {
    def apply[A](fa: KVStoreA[A]): KVStoreA[A] = fa match {
      case p@Put(key, value) =>
        println(s"$p")
        p
      case g@Get(key) =>
        println(s"$g")
        g
    }
  }

  def metricsCompiler(name:String): KVStoreA ~> KVStoreA = new (KVStoreA ~> KVStoreA) {
    var getCounter = 0L
    var putCounter = 0L

    def apply[A](fa: KVStoreA[A]): KVStoreA[A] = fa match {
      case p@Put(key, value) =>
        putCounter += 1
        println(s"Metrics[$name] - #Put: $putCounter")
        p
      case g@Get(key) =>
        getCounter += 1
        println(s"Metrics[$name] - #Get: $getCounter")
        g
    }
  }

}
