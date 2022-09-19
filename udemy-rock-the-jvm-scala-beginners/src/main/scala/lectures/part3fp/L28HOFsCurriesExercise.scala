package lectures.part3fp

object L28HOFsCurriesExercise extends App {

  /*
    1. Expand MyList
      - foreach method A => Unit
        [1, 2, 3].foreach(x => println)

      - sort function ((A, A) => Int) => MyList
        [1, 2, 3].sort((x, y) => y - x) = [3, 2, 1]

      - zipWith (list, (A, A) => B) => MyList[B]
        [1, 2, 3].zipWith([4, 5, 6], x * y) = [1 * 4, 2 * 5, 3 * 6] = [4, 10, 18]

      - fold(start)(function) => a value
        [1, 2, 3].fold(0)(x + y) = 6
        You add the first element 1 with the start value 0, and then you ad the second element 2 to the previous value,
        then the third element 3 to the previous value...
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

    def map[B](transformer: A => B): MyList[B]
    def flatMap[B](transformer: A => MyList[B]): MyList[B]
    def filter(predicate: A => Boolean): MyList[A]

    // hofs
    def foreach(f: A => Unit): Unit
    def sort(compare: (A, A) => Int): MyList[A]
    def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C]
    def fold[B](start: B)(operator: (B, A) => B): B
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
    override def fold[B](start: B)(operator: (B, Nothing) => B): B = start

    // hofs
    override def foreach(f: Nothing => Unit): Unit = ()
    override def sort(compare: (Nothing, Nothing) => Int): MyList[Nothing] = Empty
    override def zipWith[B, C](list: MyList[B], zip: (Nothing, B) => C): MyList[C] =
      if (!list.isEmpty) throw new RuntimeException("Lists do not have the same length")
      else Empty
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

    // hofs
    override def foreach(f: A => Unit): Unit =
      f(h)
      t.foreach(f)

    override def sort(compare: (A, A) => Int): MyList[A] = {
      // insertion sort
      def insert(x: A, sortedList: MyList[A]): MyList[A] =
        if (sortedList.isEmpty) new Cons(x, Empty)
        else if (compare(x, sortedList.head) <= 0) new Cons(x, sortedList)  // means x is the smallest in the result
        else new Cons(sortedList.head, insert(x, sortedList.tail))

      val sortedTail = t.sort(compare)
      insert(h, sortedTail)
    }

    override def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C] =
      if (list.isEmpty) throw new RuntimeException("Lists do not have the same length")
      else new Cons(zip(h, list.head), t.zipWith(list.tail, zip))

    /*
      [1, 2, 3].fold(0)(+)
      = [2, 3].fold(1)(+)
      = [3].fold(3)(+)
      = [].fold(6)(+)
      = 6
     */
    override def fold[B](start: B)(operator: (B, A) => B): B = {
//      val newStart = operator(start, h)
//      t.fold(newStart)(operator)
      t.fold(operator(start, h))(operator)
    }
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

  listOfIntegers.foreach(println)   // should print 1 2 3 on separate lines
  println(listOfIntegers.sort((x, y) => y - x))   //>> [3 2 1]
  println(anotherListOfIntegers.zipWith[String, String](listOfStrings, _ + "-" + _))  //>> [4-Hello 5-Scala]  // type for _
  println(listOfIntegers.fold(0)(_ + _))  //>> 6

  /*
    2.  toCurry(f: (Int, Int) => Int) => (Int => Int => Int)
        fromCurry(f: (Int => Int => Int)) => (Int, Int) => Int
   */

  def toCurry(f: (Int, Int) => Int): (Int => Int => Int) =
    x => y => f(x, y)

  def fromCurry(f: (Int => Int => Int)): (Int, Int) => Int =
    (x, y) => f(x)(y)

  /*
    3.  compose(f, g) => x => f(g(x))
        andThen(f, g) => x => g(f(x))
   */

  // FunctionX
  def compose(f: Int => Int, g: Int => Int): Int => Int =
    x => f(g(x))

  def composeG[A, B, T](f: A => B, g: T => A): T => B =
    x => f(g(x))

  def andThen(f: Int => Int, g: Int => Int): Int => Int =
    x => g(f(x))

  def andThenG[A, B, C](f: A => B, g: B => C): A => C =
    x => g(f(x))

  def superAdder: (Int => Int => Int) = toCurry(_ + _)
  def add4 = superAdder(4)
  println(add4(17))   //>> 21
  def simpleAdder = fromCurry(superAdder)
  println(simpleAdder(4, 17))   //>> 21

  val add2 = (x: Int) => x + 2
  val times3 = (x: Int) => x * 3
  val composed = compose(add2, times3)
  val ordered = andThen(add2, times3)

  println(composed(4))  //>> 14
  println(ordered(4))   //>> 18
}
