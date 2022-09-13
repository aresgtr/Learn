package lectures.part3fp

object L25WhatsAFunction extends App {

  // Purpose: use functions as first class elements - we work with functions just like plain values

  trait MyFunction[A, B]{
    def apply(element: A): B
  }

  val doubler = new MyFunction[Int, Int] {
    override def apply(element: Int): Int = element * 2
  } // This is an instance of function-like class, can be called like function

  println(doubler(2)) //>> 4

  // Scala's out-of-box support for Function Types = Function1[A, B] (up to Function22)
  val stringToIntConverter = new Function1[String, Int] {
    override def apply(string: String): Int = string.toInt
  }

  println(stringToIntConverter("3") + 4) //>> 7

  val adder: ((Int, Int) => Int) = new Function2[Int, Int, Int] {
    override def apply(a: Int, b: Int): Int = a + b
  }

  // Function types syntactic sugar Function2[A, B, R] === (A, B) => R

  // ALL SCALA FUNCTIONS ARE OBJECTS
  
  /*
    Exercise
    1. a function takes 2 strings and concatenates them
    2. transform the MyPredicate and MyTransformer into function types
    3. define a function which takes an int and returns another function which takes an int and returns an int
      - what's the type of this function
      - how to do it
   */

  // Exercise 1
  def concatenator: (String, String) => String = new Function2[String, String, String] {
    override def apply(a: String, b: String): String = a + b
  }
  println(concatenator("Hello ", "Scala"))  //>> Hello Scala

  // Exercise 3
  // Type: Function1[Int, Function1[Int, Int]]
  val superAdder: Function1[Int, Function1[Int, Int]] = new Function1[Int, Function1[Int, Int]] {
    override def apply(x: Int): Function1[Int, Int] = new Function[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }
  val adder3 = superAdder(3)
  println(adder3(4))  //>> 7
  println(superAdder(3)(4))   // curried function

  // Exercise 2
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

    def map[B](transformer: A => B): MyList[B]
    def flatMap[B](transformer: A => MyList[B]): MyList[B]
    def filter(predicate: A => Boolean): MyList[A]
  }

  case object Empty extends MyList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException // "Nothing" is a proper substitute for "Any" type
    override def tail: MyList[Nothing] = throw new NoSuchElementException
    override def isEmpty: Boolean = true
    override def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)
    override def printElements: String = ""

    override def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

    override def map[B](transformer: Nothing => B): MyList[B] = Empty
    override def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty
    override def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty
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
    override def map[B](transformer: A => B): MyList[B] =
      new Cons(transformer(h), t.map(transformer))

    /*
      [1, 2].flatMap(n => [n, n + 1])
        = [1, 2] ++ [2].flatMap(n => [n, n + 1])
        = [1, 2] ++ [2, 3] ++ Empty.flatMap(n => [n, n + 1])
        = [1, 2] ++ [2, 3] ++ Empty
        = [1, 2, 2, 3]
     */
    override def flatMap[B](transformer: A => MyList[B]): MyList[B] =
      transformer(h) ++ t.flatMap(transformer)

    /*
      [1, 2, 3].filter(n % 2 == 0)
        = [2, 3].filter(n % 2 == 0)
        = new Cons(2, [3].filter(n % 2 == 0))
        = new Cons(2, Empty.filter(n & 2 == 0))
        = new Cons(2, Empty)
     */
    override def filter(predicate: A => Boolean): MyList[A] =
      if (predicate(h)) new Cons(h, t.filter(predicate))
      else t.filter(predicate)
  }

  // MyList tests
  val emptyListOfIntegers: MyList[Int] = Empty
  val emptyListOfStrings: MyList[String] = Empty

  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val listOfStrings: MyList[String] = new Cons("Hello", new Cons("Scala", Empty))

  println(listOfIntegers.toString) //>> [1 2 3]
  println(listOfStrings.toString) //>> [Hello Scala]

  println(listOfIntegers.map(new Function[Int, Int] {
    override def apply(elem: Int): Int = elem * 2
  }).toString)

  println(listOfIntegers.filter(new Function[Int, Boolean] {
    override def apply(elem: Int): Boolean = elem % 2 == 0
  }).toString)  //>> [2]

  val anotherListOfIntegers: MyList[Int] = new Cons(4, new Cons(5, Empty))
  println((listOfIntegers ++ anotherListOfIntegers).toString) //>> [1 2 3 4 5]

  println(listOfIntegers.flatMap(new Function1[Int, MyList[Int]] {
    override def apply(elem: Int): MyList[Int] = new Cons(elem, new Cons(elem + 1, Empty))
  }).toString) //>> [1 2 2 3 3 4]

  val cloneListOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(cloneListOfIntegers == listOfIntegers)  //>> true
}

/**
 * Takeaways
 *
 * We want to work with functions
 * - pass functions as parameters
 * - use functions as values
 *
 * Problem: Scala works on top of the JVM
 * - designed for Java
 * - first-class elements: objects (instances of classes)
 *
 * Solution: ALL Scala functions are objects!
 * - function traits up to 22 params
 *    example:  trait Function1[-A, +B] {
 *                def apply(element: A): B
 *              }
 * - syntactic sugar function types
 *    example:  Function2[Int, String, Int]
 *              (Int, String) => Int
 */

