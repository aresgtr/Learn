package lectures.part3fp

object L26AnonymousFunctions extends App {

  val doubler0 = new Function[Int, Int] {
    override def apply(x: Int): Int = x * 2
  }
  // This is still the object-oriented way of defining a anonymous function and instantiate it on the spot

  // Anonymous function (LAMBDA)
  val doubler1 = (x: Int) => x * 2
  val doubler2: Int => Int = x => x * 2

  // Multiple params in LAMBDA - you have to put them in ()
  val adder: (Int, Int) => Int = (a: Int, b: Int) => a + b

  // No params
  val justDoSomething: () => Int = () => 3
  println(justDoSomething())  // You have to call functions with () in LAMBDA

  // Curly Braces with LAMBDA
  val stringToInt = { (str: String) =>
    str.toInt
  }

  // More Syntactic Sugar
  val niceIncrementer: Int => Int = _ + 1     // equivalent to x => x + 1
  val niceAdder: (Int, Int) => Int = _ + _    // equivalent to (a, b) => a + b

  /*
    1. MyList: replace all FunctionX calls with lambdas
    2. Rewrite the "special" adder as an anonymous function
   */

  // Exercise 2
  val superAdd = (x: Int) => (y: Int) => x + y
  println(superAdd(3)(4))   //>> 7

  // Exercise 1
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

  println(listOfIntegers.map((elem: Int) => elem * 2).toString)

  println(listOfIntegers.filter((elem: Int) => elem % 2 == 0).toString)  //>> [2]

  val anotherListOfIntegers: MyList[Int] = new Cons(4, new Cons(5, Empty))
  println((listOfIntegers ++ anotherListOfIntegers).toString) //>> [1 2 3 4 5]

  println(listOfIntegers.flatMap((elem: Int) => new Cons(elem, new Cons(elem + 1, Empty))).toString) //>> [1 2 2 3 3 4]

  val cloneListOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(cloneListOfIntegers == listOfIntegers)  //>> true
}
