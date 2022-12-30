package lectures.part1as



object L06AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
    case _ =>
  } //>> the only element is 1

  /**
   * Structure available for Pattern Matching
   * - constants
   * - wildcards (_)
   * - case classes
   * - tuples
   * - some special magic like above
   */

  // Sometimes for some reasons, you cannot make the below class a case class. However, you still want to use pattern matching.
  class Person(val name: String, val age: Int)

  // To make it compatible with pattern matching, you start by defining the companion object
  object Person {
    // Define a special method "unapply"
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    // We can overload unapply
    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)

  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a yo."
  }

  println(greeting) //>> Hi, my name is Bob and I am 25 yo.


  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)  //>> My legal status is major

  /*
      Exercise

      val n: Int = 45
      val mathProperty = n match {
        case x if x < 10 => "single digit"
        case x if x % 2 == 0 => "an even number"
        //...
        case _ => "no property"
      }

      The above is too wordy. Design a better solution.
  */

  object even {
    def unapply(arg: Int): Option[Boolean] =
      if (arg % 2 == 0) Some(true)
      else None
  }

  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
      if (arg > -10 && arg < 10) Some(true)
      else None
  }

  val n: Int = 8
  val mathProperty = n match {
    case singleDigit(_) => "single digit"
    case even(_) => "an even number"
    //...
    case _ => "no property"
  }

  println(mathProperty) //>> single digit
}

object Note {
  // Another technique to make the above code even nicer
  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n: Int = 8
  val mathProperty = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    //...
    case _ => "no property"
  }
}

object Part2 extends App {

  // infix patterns
  case class Or[A, B](a: A, b: B) // Similar to Either
  val either = Or(2, "two")
  val humanDescription = either match {
    case Or(number, string) => s"$number is written as $string"
    case number Or string => s"$number is written as $string" // exactly the same as above, more readable
  }
  println(humanDescription) //>> 2 is written as two

  // decomposing sequences
  val numbers = List(1)
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }
  println(decomposed) //>> starting with 1, 2

  // custom return types for unapply
  // isEmpty: Boolean, get: something
  import lectures.part1as.L06AdvancedPatternMatching.Person
  val bob = new Person("Bob", 25)

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An alien"
  })  //>> This person's name is Bob

}