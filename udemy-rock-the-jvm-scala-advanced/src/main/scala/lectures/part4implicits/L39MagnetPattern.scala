package lectures.part4implicits

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object L39MagnetPattern extends App {

  /**
   * Magnet Pattern - Aims to solve problems caused by Method Overloading
   */

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message: T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // lots of overloads
  }

  /*
    This causes problems:
      1 - type erasure
        example: if we have another receive of Future, it will not compile ⬇️
        def receive(future: Future[P2PResponse]): Int

      2 - lifting doesn't work for all overloads
        val receiveFV = receive _   // ?!

      3 - code duplication
      4 - type inference and default args

        actor.receive(?!)
   */

  // improve

  trait MessageMagnet[Result] {   // magnet acts like the center, and all other stuff are happening around it
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling P2PRequest
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling P2PResponse
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest)   //>> Handling P2P request
  receive(new P2PResponse)  //>> Handling P2P response

  // Benefits
  // 1 - no more type erasure problems!
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PRequest)))    //>> 3
  println(receive(Future(new P2PResponse)))   //>> 2

  // 2 - lifting works
  // Say you are working for a data processing project
  trait MathLib {
    def add1(x: Int) = x + 1
    def add1(s: String) = s.toInt + 1
    // add1 overloads
  }

  // "magnetize"
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))     //>> 2
  println(addFV("3"))   //>> 4

  /*
    Drawbacks
    1 - verbose
    2 - harder to read
    3 - you can't name or place default args
    4 - call by name doesn't work correctly
      (exercise: prove it!) (hint: side effects)
   */

  // exercise
  class Handler {
    def handle(s: => String): Unit = {
      println(s)
      println(s)
    }
    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "hahaha"
  }

  handle(sideEffectMethod())
  //>> Hello, Scala
  //>> hahaha
  //>> Hello, Scala
  //>> hahaha

  handle {
    println("Hello, Scala")
    "hahaha"
  }
  //>> Hello, Scala
  //>> hahaha
  //>> hahaha         ⬅️ careful

  // ⬆️ equivalent
  handle {
    println("Hello, Scala")
    new StringHandle("hahaha")
  }
}
