package lectures.part4pattern_matching

import scala.util.Random

object L36PatternMatching extends App {
  // switch on steroids
  val random = new Random
  val x = random.nextInt(10)  // any number from 0 to 10

  val description = x match {
    case 1 => "the ONE"
    case 2 => "double or nothing"
    case 3 => "third time is the charm"
    case _ => "something else"  // _ = WILDCARD
  }

  println(x)  //e.g.>> 1
  println(description)  //e.g.>> the ONE

  // Properties
  // 1. Decompose values - case classes have the ability to be deconstructed or extracted in pattern matching
  case class Person(name: String, age: Int)
  val bob = Person("Bob", 20)

  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi my name is $n and I can't drink in the US"
    case Person(n, a) => s"Hi my name is $n and I am $a years old"
    case _ => "I don't know who I am"
  }
  println(greeting) //>> Hi my name is Bob and I can't drink in the US
  // We can not only pattern match against any value, we can pattern match a case class pattern and extract values


}
