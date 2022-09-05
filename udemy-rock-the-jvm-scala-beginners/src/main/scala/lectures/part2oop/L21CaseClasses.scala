package lectures.part2oop

object L21CaseClasses extends App {

  /**
   * The problem we want to solve:
   * Often for lightweight data structures (like class Person) we need to implement boilerplate
   * e.g. companion objects, standard methods for serializing & pretty printing (equals, hashCode, toString)
   */

  case class Person(name: String, age: Int)

  // 1. Class parameters are fields
  val jim = new Person("Jim", 34)
  println(jim.name) // this is valid

  // 2. Sensible toString
  println(jim.toString) //>> Person(Jim,34)
  println(jim)  // equivalent, println(instance) = println(instance.toString)

  // 3. equals and hashCode implemented out of box
  val jim2 = new Person("Jim", 34)
  println(jim == jim2)  //>> true

  // 4. Have handy copy method
  val jim3 = jim.copy(age = 45)

  // 5. Have companion objects
  val thePerson = Person  // auto create companion objects
  val mary = Person("Mary", 23) // The companion object's apply() method does the same thing as the constructor.
  // So in practice, we don't really use the keyword new when instantiating a case class. We only use the above form.

  // 6. Serializable
  // Case classes are especially useful when dealing with distributed systems. You can send instances of case classes
  // through the network and in between JVMs. This is important for Akka framework.
  // Akka - sending serializable messages through the network, and messages are in general case classes.

  // 7. Have extractor pattern = can be used in PATTERN MATCHING

  /**
   * Case objects have the same property as case classes, except they don't get companion objects because
   * they are their own companion objects.
   */
  case object UnitedKingdom {
    def name: String = "The UK of GB and NI"
  }


  /*
    Exercise: expand MyList with case classes and case objects (only add two words)
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

    // concatenation function for flatMap
    def ++[B >: A](list: MyList[B]): MyList[B]

    def map[B](transformer: MyTransformer[A, B]): MyList[B]
    def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B]
    def filter(predicate: MyPredicate[A]): MyList[A]
  }

  case object Empty extends MyList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException // "Nothing" is a proper substitute for "Any" type
    override def tail: MyList[Nothing] = throw new NoSuchElementException
    override def isEmpty: Boolean = true
    override def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)
    override def printElements: String = ""

    override def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

    override def map[B](transformer: MyTransformer[Nothing, B]): MyList[B] = Empty
    override def flatMap[B](transformer: MyTransformer[Nothing, MyList[B]]): MyList[B] = Empty
    override def filter(predicate: MyPredicate[Nothing]): MyList[Nothing] = Empty
  }

  case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
    override def head: A = h
    override def tail: MyList[A] = t
    override def isEmpty: Boolean = false
    override def add[B >: A](element: B): MyList[B] = new Cons(element, this)
    override def printElements: String =
      if (t.isEmpty) "" + h
      else "" + h + " " + t.printElements

    /*
      [1, 2] ++ [3, 4, 5]
        = new Cons(1, [2] ++ [3, 4, 5])
        = new Cons(1, new Cons(2, Empty ++ [3, 4, 5]))
        = new Cons(1, new Cons(2, new Cons(3, new Cons(4, new Cons(5)))))
     */
    override def ++[B >: A](list: MyList[B]): MyList[B] = new Cons(h, t ++ list)

    /*
      [1, 2, 3].map(n * 2)
        = new Cons(2, [2, 3].map(n * 2))
        = new Cons(2, new Cons(4, [3].map(n * 2)))
        = new Cons(2, new Cons(4, new Cons(6, Empty.map(n * 2))))
        = new Cons(2, new Cons(4, new Cons(6, Empty))
     */
    override def map[B](transformer: MyTransformer[A, B]): MyList[B] =
      new Cons(transformer.transform(h), t.map(transformer))

    /*
      [1, 2].flatMap(n => [n, n + 1])
        = [1, 2] ++ [2].flatMap(n => [n, n + 1])
        = [1, 2] ++ [2, 3] ++ Empty.flatMap(n => [n, n + 1])
        = [1, 2] ++ [2, 3] ++ Empty
        = [1, 2, 2, 3]
     */
    override def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B] =
      transformer.transform(h) ++ t.flatMap(transformer)

    /*
      [1, 2, 3].filter(n % 2 == 0)
        = [2, 3].filter(n % 2 == 0)
        = new Cons(2, [3].filter(n % 2 == 0))
        = new Cons(2, Empty.filter(n & 2 == 0))
        = new Cons(2, Empty)
     */
    override def filter(predicate: MyPredicate[A]): MyList[A] =
      if (predicate.test(h)) new Cons(h, t.filter(predicate))
      else t.filter(predicate)
  }

  trait MyPredicate[-T] {
    def test(elem: T): Boolean
  }

  trait MyTransformer[-A, B] {
    def transform(elem: A): B
  }

  // MyList tests
  val emptyListOfIntegers: MyList[Int] = Empty
  val emptyListOfStrings: MyList[String] = Empty

  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val listOfStrings: MyList[String] = new Cons("Hello", new Cons("Scala", Empty))

  println(listOfIntegers.toString) //>> [1 2 3]
  println(listOfStrings.toString) //>> [Hello Scala]

  println(listOfIntegers.filter(new MyPredicate[Int] {
    override def test(elem: Int): Boolean = elem % 2 == 0
  }).toString)  //>> [2]

  val anotherListOfIntegers: MyList[Int] = new Cons(4, new Cons(5, Empty))

  println((listOfIntegers ++ anotherListOfIntegers).toString) //>> [1 2 3 4 5]
  println(listOfIntegers.flatMap(new MyTransformer[Int, MyList[Int]] {
    override def transform(elem: Int): MyList[Int] = new Cons(elem, new Cons(elem + 1, Empty))
  }).toString) //>> [1 2 2 3 3 4]

  val cloneListOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(cloneListOfIntegers == listOfIntegers)  //>> true
}
