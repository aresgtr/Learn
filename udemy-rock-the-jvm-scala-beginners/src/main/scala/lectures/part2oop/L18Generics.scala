package lectures.part2oop

object L18Generics extends App {

  object Intro {

    class MyList[A] { // this also works with traits
      // use the type parameter A
    }

    val listOfIntegers = new MyList[Int]
    val listOfStrings = new MyList[String]

    // you can also have multiple type parameters
    class MyMap[Key, Value]

    // Generic Methods
    object MyList { // object cannot be type parameterized
      def empty[A]: MyList[A] = ???
    }

    val emptyListOfInt = MyList.empty[Int]

    /*
      Variance Problem - if cat extends animal, does a list of cat also extend the list of animal?
     */
    class Animal

    class Cat extends Animal

    class Dog extends Animal

    // 1. Yes, List[Cat] extends List[Animal] - COVARIANCE
    class CovariantList[+A]

    val animal: Animal = new Cat
    val animalList: CovariantList[Animal] = new CovariantList[Cat]
    // In this case, animalList.add(new Dog) is this ok??? HARD QUESTION.
    // animalList is defined to be a list of animals. In theory, I should be able to add any animal.
    // But adding a new dog to an animalList, which in fact is a list of Cats, will pollute the list.
    // Adding a dog to a list of cats will turn the list into a list of generic animals.

    // 2. No, List[Cat] and List[Animal] are two separate things - INVARIANCE
    class InvariantList[A]
    //  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Cat] // Cannot compile

    // 3. Hell, no! (very counterintuitive) - CONTRAVARIANCE, opposite of covariance
    class Trainer[-A]

    val trainer: Trainer[Cat] = new Trainer[Animal] // an animal trainer can train any type of animals, including cats

    /*
      Bounded Types
     */
    // Upper bounded type
    class Cage[A <: Animal](animal: A) // class cage only accepts type parameter A, which is the subtype of Animal

    val cage = new Cage(new Dog)

    // Lower bounded type
    class Cage2[A >: Animal] // class cage2 only accepts something that is a super type of animal
  }

  object CovarianceExample {

    // Implementation for Covariant List
    class MyList[+A] {
      //    def add(element: A): MyList[A] = ???  // This does not compile
      def add[B >: A](element: B): MyList[B] = ???
      // add method will take another type parameter B, which is a super type of A, will return Mylist of type B
      // "Adding a dog to a list of cats will turn the list into a list of generic animals."
    }
  }

  /*
    Exercise: Expand MyList to be Generic
   */

  abstract class MyList[+A] { // immutable
    /*
      head = first element of the list
      tail = remainder of the list
      isEmpty = is this list empty?
      add(int) => new list with this element added
      toString => a string representation of the list
     */

    def head: A

    def tail: MyList[A]

    def isEmpty: Boolean

    def add[B >: A](element: B): MyList[B]

    def printElements: String

    // polymorphic call
    override def toString: String = "[" + printElements + "]"
  }

  object Empty extends MyList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException // "Nothing" is a proper substitute for "Any" type

    override def tail: MyList[Nothing] = throw new NoSuchElementException

    override def isEmpty: Boolean = true

    override def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)

    override def printElements: String = ""
  }

  class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
    override def head: A = h

    override def tail: MyList[A] = t

    override def isEmpty: Boolean = false

    override def add[B >: A](element: B): MyList[B] = new Cons(element, this)

    override def printElements: String =
      if (t.isEmpty) "" + h
      else "" + h + " " + t.printElements
  }

  // MyList tests
  val emptyListOfIntegers: MyList[Int] = Empty
  val emptyListOfStrings: MyList[String] = Empty

  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val listOfStrings: MyList[String] = new Cons("Hello", new Cons("Scala", Empty))

  println(listOfIntegers.toString) //>> [1 2 3]
  println(listOfStrings.toString) //>> [Hello Scala]
}

