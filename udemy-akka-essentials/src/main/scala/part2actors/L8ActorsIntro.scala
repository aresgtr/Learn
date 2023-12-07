package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object L8ActorsIntro extends App {

  // Part 1 - Actor Systems
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name) //>> firstActorSystem

  // Part 2 - Create Actors
  // - Actors are uniquely identified (like human with ID, SIN, etc.)
  // - Messages are asynchronous (you send the message when you need, and the actors reply when they can)
  // - Each actor may respond differently (if a human hate you, they might not reply back)
  // - Actors are (really) encapsulated (you can't read their mind)

  // word count actor
  class WordCountActor extends Actor {
    // internal data
    var totalWords = 0

    // behavior
    override def receive: PartialFunction[Any, Unit] = {
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords += message.split(" ").length
      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }

  // Part 3 - Instantiate Our Actor
  val wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter = actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")

  // Part 4 - Communicate!
  wordCounter ! "I am learning Akka and it's pretty damn cool!" // "tell"
  anotherWordCounter ! "A different message"
  // ⬆️ asynchronous!
  //>> [word counter] I have received: A different message
  //>> [word counter] I have received: I am learning Akka and it's pretty damn cool!

  // How do we instantiate an Actor with constructor arguments?
  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"Hi, my name is $name")
      case _ =>
    }
  }

  val person = actorSystem.actorOf(Props(new Person("Bob")))  // This is discouraged, see 👇
  person ! "hi" //>> Hi, my name is Bob

}

object L8BestPractice extends App {
  val actorSystem = ActorSystem("firstActorSystem")

  // best practice of ⬆️ - companion object as the factory
  object Person {
    def props(name:String) = Props(new Person(name))
  }

  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"Hi, my name is $name")
      case _ =>
    }
  }

  val person = actorSystem.actorOf(Person.props("Bob"))
  person ! "hi" //>> Hi, my name is Bob
}
