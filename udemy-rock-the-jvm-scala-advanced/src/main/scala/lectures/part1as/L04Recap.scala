package lectures.part1as

import scala.annotation.tailrec

object L04Recap extends App {

  val aCondition: Boolean = false
  val aConditionCal = if (aCondition) 42 else 65

  /**
   *  Instructions vs Expressions
   *
   *  Instructions are fundamental operation of imperative languages like Java, Python, or C++
   *    - Executed in sequence to do things for programs
   *
   *  Whereas in functional languages like Scala, we construct programs through expressions
   *    - by building expressions on top of other expressions
   *    - e.g. code blocks
   */

  // compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit = void
  val theUnit: Unit = println("Hello, Scala")

  // functions
  def aFunction(x: Int): Int = x + 1

  // Recursion: stack and tail
  @tailrec
  def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)

  // Object-oriented programming
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog  // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // Method Notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog  // similar to natural language

  // Anonymous Classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar!")
  }

  // Generics
  abstract class MyList[+A] // variance and variance problems in THIS course

  // singletons and companions
  object MyList

  // case classes
  case class Person(name: String, age: Int)

  // exceptions and try/catch/finally
  val throwsException = throw new RuntimeException  // type Nothing
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught an exception"
  } finally {
    println("some logs")
  }

  // packaging and imports

  // Functional Programming
  // Apply method allows instances and singleton objects to be "called" like they were functions.
  // Functions are actually instances of classes with apply methods.
  val incrementer = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  // first-class language support for lambdas/anonymous function
  val anonymousIncrementor = (x: Int) => x + 1
  List(1, 2, 3).map(anonymousIncrementor) // map, flatMap, filter: higher order functions

  // for-comprehension
  val pairs = for {
    num <- List(1, 2, 3)
    char <- List('a', 'b', 'c')
  } yield num + "-" + char  // cross-pairing

  println(pairs)  //>> List(1-a, 1-b, 1-c, 2-a, 2-b, 2-c, 3-a, 3-b, 3-c)

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // "collections": Options, Try
  val anOption = Some(2)

  // Pattern matching
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

  // all the patterns
}
