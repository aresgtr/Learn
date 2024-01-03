package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.L15ChildActors.CreditCard.CheckStatus
import part2actors.L15ChildActors.NaiveBankAccount.{Deposit, InitializeAccount}

object L15ChildActors extends App {

  // Actors can create other actors

  object Parent {
    case class CreateChild(name: String)
    case class TellChild(message: String)
  }

  class Parent extends Actor {
    import Parent._

    override def receive: Receive = {
      case CreateChild(name) =>
        println(s"${self.path} creating child")
        // create a new actor right HERE
        val childRef = context.actorOf(Props[Child], name)
        context.become(withChild(childRef))
    }

    def withChild(childRef: ActorRef): Receive = {
      case TellChild(message) =>
        if (childRef != null) childRef forward message
    }
  }

  class Child extends Actor {
    override def receive: Receive = {
      case message => println(s"${self.path} I got: $message")
    }
  }

  import Parent._

  val system = ActorSystem("ParentChildDemo")
  val parent = system.actorOf(Props[Parent], "parent")
  parent ! CreateChild("child")
  //>> akka://ParentChildDemo/user/parent creating child
  parent ! TellChild("hey kid!")
  //>> akka://ParentChildDemo/user/parent/child I got: hey kid!

  //                            ⇧ hierarchy

  /**
   * Actor hierarchies
   *
   * parent -> child -> grandChild
   *        -> child2
   *
   * > Question: child is owned by parent, then who owns parent?  - Guardian Actors (top-level)
   *
   * 3 Guardian Actors
   * - /system = system guardian
   * - /user = user-level guardian
   * - / = the root guardian
   */

  /**
   * Actor Selection
   */
  val childSelection = system.actorSelection("/user/parent/child")  // If the path is wrong, dead letters happens
  childSelection ! "I found you!"
  //>> akka://ParentChildDemo/user/parent/child I got: I found you!

  /**
   * Danger!
   *
   * NEVER PASS MUTABLE ACTOR STATE, OR THE `THIS` REFERENCE, TO CHILD ACTORS
   *
   * ⇧ This is called `Closing over`
   *
   * Never close over mutable state or `this`!
   */
  object NaiveBankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object InitializeAccount
  }

  class NaiveBankAccount extends Actor {
    import NaiveBankAccount._
    import CreditCard._

    var amount = 0

    override def receive: Receive = {
      case InitializeAccount =>
        val creditCardRef = context.actorOf(Props[CreditCard], "card")
        creditCardRef ! AttachToAcount(this) // !! breaking actor encapsulation. only call actor through messages
      case Deposit(funds) => deposit(funds)
      case Withdraw(funds) => withdraw(funds)
    }

    def deposit(funds: Int) = {
      println(s"${self.path} depositing $funds on top of $amount")
      amount += funds
    }

    def withdraw(funds: Int) = {
      println(s"${self.path} withdrawing $funds from $amount")
      amount -= funds
    }
  }

  object CreditCard {
    case class AttachToAcount(bankAccount: NaiveBankAccount)  // !! (problem: should use ActorRef)
    case object CheckStatus
  }

  class CreditCard extends Actor {
    import CreditCard._

    override def receive: Receive = {
      case AttachToAcount(account) => context.become(attachedTo(account))
    }

    def attachedTo(account: NaiveBankAccount): Receive = {
      case CheckStatus =>
        println(s"${self.path} your message has been processed.")
        // benign
        account.withdraw(1) // !! because I can (problem: we never directly call methods on actors)
    }
  }

  val bankAccountRef = system.actorOf(Props[NaiveBankAccount], "account")
  bankAccountRef ! InitializeAccount
  bankAccountRef ! Deposit(100)
  //>> akka://ParentChildDemo/user/account depositing 100 on top of 0

  Thread.sleep(500)
  val creditCardSelection = system.actorSelection("/user/account/card")
  creditCardSelection ! CheckStatus
  //>> akka://ParentChildDemo/user/account/card your message has been processed.
  //>> akka://ParentChildDemo/user/account withdrawing 1 from 100                 // WRONG!!!
}
