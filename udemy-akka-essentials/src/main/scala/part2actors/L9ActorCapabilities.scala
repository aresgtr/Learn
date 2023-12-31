package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.L10Exercise.Counter.{Decrement, Increment, Print}
import part2actors.L10Exercise.Person.LiveTheLive

object L9ActorCapabilities extends App {

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message: String => println(s"[simple actor] I have received $message")
      case number: Int => println(s"[simple actor] I have received a NUMBER: $number")
      case SpecialMessage(contents) => println(s"[simple actor] I have received something SPECIAL: $contents")
    }
  }

  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  simpleActor ! "hello, actor"  //>> [simple actor] I have received hello, actor

  // 1 - Messages can be of any type
  // a) messages must be IMMUTABLE
  // b) messages must be SERIALIZABLE
  // - in practice, use case classes and case objects
  simpleActor ! 42  //>> [simple actor] I have received a NUMBER: 42

  // you can also define your own type
  case class SpecialMessage(contents: String)
  simpleActor ! SpecialMessage("some special content")  //>> [simple actor] I have received something SPECIAL: some special content

}

object L9P2 extends App {
  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  // 2- Actors have information about their context and about themselves
  // context.self === `this` in OOP

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message: String => println(s"[$self] I have received $message")
      case SendMessageToYourself(content) => self ! content
    }
  }

  case class SendMessageToYourself(context: String)
  simpleActor ! SendMessageToYourself("I am an actor and I am proud of it")
  //>> [Actor[akka://actorCapabilitiesDemo/user/simpleActor#395796280]] I have received I am an actor and I am proud of it
}

object L9P3 extends App {
  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case "Hi!" => context.sender() ! "Hello, there!"  // replying to a message
      case message: String => println(s"[$self] I have received $message")
      case SayHiTo(ref) => ref ! "Hi!"
      case WirelessPhoneMessage(content, ref) => ref forward (content + "s") // i keep the original sender of the WPM
    }
  }

  // 3 - Actors can REPLY to messages
  val alice = system.actorOf(Props[SimpleActor], "alice")
  val bob = system.actorOf(Props[SimpleActor], "bob")

  case class SayHiTo(ref: ActorRef)
  alice ! SayHiTo(bob)
  //>> [Actor[akka://actorCapabilitiesDemo/user/alice#1175041182]] I have received Hello, there!

  // 4 - dead letters, if not received because of empty "self"
  alice ! "Hi!"
  // (这个可以理解成"我"在上帝视角tell alice了个Hi！但alice找不到"我"是谁，没办法reply back)

  // 5 - forwarding messages
  // Daniel -> Alice -> Bob
  // forwarding = sending a message with the ORIGINAL sender

  case class WirelessPhoneMessage(content: String, ref: ActorRef)
  alice ! WirelessPhoneMessage("Hi", bob) // noSender.
  //>> [Actor[akka://actorCapabilitiesDemo/user/bob#-1545825786]] I have received His
}

object L10Exercise extends App {
  val system = ActorSystem("actorCapabilitiesEx")

  /**
   * Exercise
   *
   * 1. a Counter actor
   *  - Increment
   *  - Decrement
   *  - Print
   */

  // DOMAIN of the counter (companion obj)
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter extends Actor {
    import Counter._

    var count = 0

    override def receive: Receive = {
      case Increment => count += 1
      case Decrement => count -= 1
      case Print => println(s"[counter] My current count is $count")
    }
  }

  val counter = system.actorOf(Props[Counter], "myCounter")

  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Decrement)
  counter ! Print
  //>> [counter] My current count is 2

  /**
   * 2. a Bank account as an actor
   *  receives
   *  - Deposit an amount
   *  - Withdraw an amount
   *  - Statement
   *  replies with
   *  - Sucess
   *  - Failure
   *
   *  interact with some other kind of actors
   */

  // DOMAIN
  object BankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object Statement

    case class TransactionSuccess(message: String)
    case class TransactionFailure(reason: String)
  }

  class BankAccount extends Actor {
    import BankAccount._

    var funds = 0

    override def receive: Receive = {
      case Deposit(amount) =>
        if (amount < 0) sender() ! TransactionFailure("invalid deposit amount")
        else {
          funds += amount
          sender() ! TransactionSuccess(s"successfully deposited $amount")
        }

      case Withdraw(amount) =>
        if (amount < 0) sender() ! TransactionFailure("invalid withdraw amount")
        else if (amount > funds) sender() ! TransactionFailure("insufficient funds")
        else {
          funds -= amount
          sender() ! TransactionSuccess(s"successfully withdrew $amount")
        }

      case Statement => sender() ! s"Your balance is $funds"
    }
  }

  object Person {
    case class LiveTheLive(account: ActorRef)
  }

  class Person extends Actor {
    import Person._
    import BankAccount._

    override def receive: Receive = {
      case LiveTheLive(account) =>
        account ! Deposit(10)
        account ! Withdraw(90)
        account ! Withdraw(5)
        account ! Statement

      case message => println(message.toString)
    }
  }

  val account = system.actorOf(Props[BankAccount], "bankAccount")
  val person = system.actorOf(Props[Person], "Tom")

  person ! LiveTheLive(account)
  //>> TransactionSuccess(successfully deposited 10)
  //>> TransactionFailure(insufficient funds)
  //>> TransactionSuccess(successfully withdrew 5)
  //>> Your balance is 5
}
