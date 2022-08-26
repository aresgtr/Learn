package lectures.part2oop

import scala.language.postfixOps

object L12MethodNotations extends App {

  class Person(val name: String, favoriteMovie: String, val age: Int = 0) {
    def likes(movie: String): Boolean = movie == favoriteMovie
    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def unary_! : String = s"$name, What the heck?!"  // the space before : is required, otherwise become part of operator
    def isAlive: Boolean = true
    def apply(): String = s"Hi, my name is $name and I like $favoriteMovie"

    // exercise
    def +(nickname: String): Person = new Person(s"$name ($nickname)", favoriteMovie)
    def unary_+ : Person = new Person(name, favoriteMovie, age + 1)
    def learns(thing: String) = s"$name is learning $thing"
    def learnsScala = this learns "Scala"
    def apply(n: Int): String = s"$name watched $favoriteMovie $n times"
  }

  // Infix Notation - Operator Notation (syntactic sugar)
  val mary = new Person("Mary", "Inception")
  println(mary.likes("Inception"))  //>> true
  println(mary likes "Inception") // equivalent

  // "operators" in Scala (like math operator)
  val tom = new Person("Tom", "Fight Club")
  println(mary + tom)   //>> Mary is hanging out with Tom
  println(mary.+(tom))  // equivalent

  println(1 + 2)
  println(1.+(2)) // equivalent

  // ALL OPERATORS ARE METHODS
  // e.g. Akka actors have ! ?

  // Prefix Notation
  val x = -1  // equivalent with 1.unary_-
  val y = 1.unary_-
  // unary_ prefix only works with - + ~ !
  println(!mary)          //>> Mary, What the heck?!
  println(mary.unary_!)   // equivalent

  // Postfix Notation (not normally used, not easy to ready)
  println(mary.isAlive)   //>> true
  println(mary isAlive)   // equivalent

  // Apply
  println(mary.apply())   //>> Hi, my name is Mary and I like Inception
  println(mary())         // equivalent

  /*
    Exercise

    1.  Overload the + operator
        mary + "the rockstar" => new person "Mary (the rockstar)"

    2.  Add an age to the Person class
        Add a unary + operator => new person with age + 1
        +mary => mary with the age incrementer

    3.  Add a "learns" method in the Person class => "Mary learns Scala"
        Add a learnsScala method, calls learns method with "Scala"
        Use it in postfix notation

    4.  Overload the apply method
        mary.apply(2) => "Mary watched Interception 2 times"
   */

  println((mary + "the Rockstar")())  //>> Hi, my name is Mary (the Rockstar) and I like Inception
  println((+mary).age)  //>> 1
  println(mary learnsScala) //>> Mary is learning Scala
  println(mary(10)) //>> Mary watched Inception 10 times
}
