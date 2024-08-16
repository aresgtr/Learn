package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object L16ChildActorsExercise extends App {

  // Distributed Word Counting

  object WordCounterMaster {
    case class Initialize(nChildren: Int)
    case class WordCountTask(/*TODO*/text: String)
    case class WordCountReply(/*TODO*/count: String)
  }

  class WordCounterMaster extends Actor {
    import WordCounterMaster._

    override def receive: Receive = {
      case Initialize(nChildren) =>
        val childrenRefs = for (i <- 1 to nChildren) yield context.actorOf(Props[WordCounterWorker], s"wcw_$i")
        context.become(withChildren(childrenRefs, 0))
    }

    def withChildren(childrenRefs: Seq[ActorRef], currentChildIndex: Int): Receive = {
      case text: String =>
        val task = WordCountTask(text)
        val childRef = childrenRefs(currentChildIndex)
        childRef ! task
        val nextChildIndex = currentChildIndex + 1 % childrenRefs.length
        context.become(withChildren(childrenRefs, nextChildIndex))
      case WordCountReply(count) =>
        // problem - who is the original sender? we need to keep track
    }
  }

  class WordCounterWorker extends Actor {
    import WordCounterMaster._

    override def receive: Receive = {
      case WordCountTask(text) => sender() ! WordCountReply(text.split(" ").length.toString)
    }
  }

  /*
    create WordCounterMaster
    send Initialize(10) to wordCounterMaster
    send "Akka is awesome" to wordCounterMaster
      wcm will send a WordCountTask("...") to one of its children
        child replies with a WordCountReply(3) to the master
      master replies with 3 to the sender.

    requester -> wcm -> wcw
           r  <- wcm <-
   */
}
