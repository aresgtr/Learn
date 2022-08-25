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

  /*
    1. Cases are match in order
    2. What if no cases match? MatchError
    3. type of the pattern matching expression? Unified type of all the types in all the cases
    4. Pattern Matching works really well with case classes*
   */

  // Pattern Matching on sealed hierarchies
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal: Animal = Dog("Terra Nova")
  animal match {
    case Dog(someBreed) => println(s"Matched a dog of the $someBreed breed")
  }
  // The above code should not compile because the matching doesn't cover all case classes (however it compiles locally)
  // If you remove sealed, it compiles. Sealed classes help you to check.

  /*
    Exercise
    simple function uses Pattern Matching
    takes an Expr => human readable form

    Sum(Number(2), Number(3)) => 2 + 3
    Sum(Number(2), Number(3), Number(4)) => 2 + 3 + 4
    Prod(Sum(Number(2), Number(1)), Number(3)) => (2 + 1) * 3
    Sum(Prod(Number(2), Number(1)), Number(3)) => 2 * 1 + 3
   */
  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr

  def show(e: Expr): String = e match {
    case Number(n) => s"$n"
    case Sum(e1, e2) => show(e1) + " + " + show(e2)
    case Prod(e1, e2) => {
      def mayberShowParentheses(expr: Expr) = expr match {
        case Prod(_, _) => show(expr)
        case Number(_) => show(expr)
        case _ => "(" + show(expr) + ")"
      }

      mayberShowParentheses(e1) + " * " + mayberShowParentheses(e2)
    }
  }

  println(show(Sum(Number(2), Number(3))))                    //>> 2 + 3
  println(show(Sum(Sum(Number(2), Number(3)), Number(4))))    //>> 2 + 3 + 4
  println(show(Prod(Sum(Number(2), Number(1)), Number(3))))   //>> (2 + 1) * 3
  println(show(Sum(Prod(Number(2), Number(1)), Number(3))))   //>> 2 * 1 + 3
}
