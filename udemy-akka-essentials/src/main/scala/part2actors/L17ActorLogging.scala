package part2actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging

object L17ActorLogging extends App {
  /*
    1 - DEBUG
    2 - INFO
    3 - WARNING/WARN
    4 - ERROR
   */

  // #1 - explicit logging
  class SimpleActorWithExplicitLogger extends Actor {
    val logger = Logging(context.system, this)

    override def receive: Receive = {
      case message => logger.info(message.toString)// LOG it
    }
  }

  val system = ActorSystem("LoggingDemo")
  val actor = system.actorOf(Props[SimpleActorWithExplicitLogger])

  actor ! "Logging a simple message"
  //>> [INFO] [01/03/2024 17:07:44.519] [LoggingDemo-akka.actor.default-dispatcher-4] [akka://LoggingDemo/user/$a] Logging a simple message

  // #2 - ActorLogging
  class ActorWithLogging extends Actor with ActorLogging {
    override def receive: Receive = {
      case (a, b) => log.info("Two things: {} and {}", a, b)  // interpolate
      case message => log.info(message.toString)
    }
  }

  val simplerActor = system.actorOf(Props[ActorWithLogging])
  simplerActor ! "Logging a simple message by extending a trait"
  //>> [INFO] [01/03/2024 17:11:38.539] [LoggingDemo-akka.actor.default-dispatcher-5] [akka://LoggingDemo/user/$b] Logging a simple message by extending a trait
  // same result as the 1st one

  simplerActor ! (42, 65)
  //>> [INFO] [01/03/2024 17:15:42.736] [LoggingDemo-akka.actor.default-dispatcher-5] [akka://LoggingDemo/user/$b] Two things: 42 and 65
}

/**
 * Logging is asynchronous
 * - Akka logging is done with actors!
 *
 * You can change the logger, e.g. SLF4J
 */