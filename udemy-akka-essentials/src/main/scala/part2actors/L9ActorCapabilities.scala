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

  // 1️⃣ - Messages can be of any type
  // a) messages must be IMMUTABLE
  // b) messages must be SERIALIZABLE
  //    - Serializable means the JVM can convert it into a byte stream and send it to another JVM, possibly over a network
  //    - Serializable is a Java Interface

  // 🧑🏻‍💻 In practice, use Case Classes and Case Objects for messages
  simpleActor ! 42  //>> [simple actor] I have received a NUMBER: 42

  // you can also define your own type
  case class SpecialMessage(contents: String)
  simpleActor ! SpecialMessage("some special content")  //>> [simple actor] I have received something SPECIAL: some special content

}

object L9P2 extends App {
  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  // 2️⃣ - Actors have information about their context and about themselves
  //      - "Context" is a complex data structure that contains information about the environment in which this Actor runs
  //          * example: context.system
  //      - context.self === `this` in OOP
  //          * === self

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

  // 3️⃣ - Actors can REPLY to messages
  val alice = system.actorOf(Props[SimpleActor], "alice")
  val bob = system.actorOf(Props[SimpleActor], "bob")

  case class SayHiTo(ref: ActorRef)
  alice ! SayHiTo(bob)  // alice tell bob "hi", bob replies "hello there"
  //>> [Actor[akka://actorCapabilitiesDemo/user/alice#1175041182]] I have received Hello, there!

  // 4️⃣ - dead letters (garbage pool), if not received because of empty "self"
  alice ! "Hi!"
  // (这个可以理解成"我"在上帝视角tell alice了个Hi！但alice找不到"我"是谁，没办法reply back)

  // 5️⃣ - forwarding messages
  // Daniel(我, the big context) -> Alice -> Bob
  // forwarding = sending a message with the ORIGINAL sender

  case class WirelessPhoneMessage(content: String, ref: ActorRef)
  alice ! WirelessPhoneMessage("Hi", bob) // noSender.
  //>> [Actor[akka://actorCapabilitiesDemo/user/bob#-1545825786]] I have received His
}
