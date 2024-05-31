package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.L10Exercise.Counter.{Decrement, Increment, Print}
import part2actors.L10Exercise.Person.LiveTheLive

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
   *  - Success
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
