package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.L13ChangingBehaviorP2.Mom.MomStart

object L13ChangingBehaviorP2 extends App {

  /**
   * context.become(anotherHandler, discardOld = true)
   *                   ⇧                  ⇧
   *   must be of type Receive     replaces current handler (default)
   *                               pass in [false] to stack the new handler on top
   */

  /**
   * context.unbecome()
   *           ⇧
   *         pops the current behavior off the stack
   */

  /**
   * Rules
   * - Akka always uses the latest handler on top of the stack
   * - if the stack is empty, it calls receive
   */

  object StackedFussyKid {
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }

  class StackedFussyKid extends Actor {
    import StackedFussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, discardOld = false) // change receive handler to sadReceive
      case Food(CHOCOLATE) => // do nothing, stay happy
      case Ask(_) => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, discardOld = false) // add another stack of sad
      case Food(CHOCOLATE) => context.unbecome() // remove a stack of sad 去掉一层sad
      case Ask(_) => sender() ! KidReject
    }
  }

  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String) // do you want to play?
    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {
    import Mom._
    import StackedFussyKid._

    override def receive: Receive = {
      case MomStart(kidRef) =>    // happy
        // test our interaction
        kidRef ! Food(VEGETABLE)  // happy, sad
        kidRef ! Food(VEGETABLE)  // happy, sad, sad (多给一次蔬菜就多一层悲伤)
        kidRef ! Ask("do you want to play?")
        kidRef ! Food(CHOCOLATE)  // happy, sad
        kidRef ! Ask("do you want to play?")
        kidRef ! Food(CHOCOLATE)  // happy
        kidRef ! Ask("do you want to play?")
      case KidAccept => println("Yay, my kid is happy!")
      case KidReject => println("My kid is sad, but he's healthy!")
    }
  }

  val system = ActorSystem("changingActorBehaviorDemo")
  val mom = system.actorOf(Props[Mom])
  val kid = system.actorOf(Props[StackedFussyKid])

  mom ! MomStart(kid)
  //>> My kid is sad, but he's healthy!
  //>> My kid is sad, but he's healthy!
  //>> Yay, my kid is happy!
}
