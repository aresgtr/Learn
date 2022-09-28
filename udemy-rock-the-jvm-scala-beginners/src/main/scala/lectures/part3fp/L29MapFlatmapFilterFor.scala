package lectures.part3fp

object L29MapFlatmapFilterFor extends App {

  val list = List(1, 2, 3)
  println(list) //>> List(1, 2, 3)
  println(list.head)  //>> 1
  println(list.tail)  //>> List(2, 3)

  // map
  println(list.map(_ + 1))  //>> List(2, 3, 4)
  println(list.map(_ + " is a number")) //>> List(1 is a number, 2 is a number, 3 is a number)

  // filter
  println(list.filter(_ % 2 == 0))  //>> List(2)

  // flatMap
  val toPair = (x: Int) => List(x, x + 1)
  println(list.flatMap(toPair)) //>> List(1, 2, 2, 3, 3, 4)

  /*
    Print all combinations between two lists
   */
  val numbers = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  // List("a1", "a2", ... "d4")
  // In conventional programming languages, we use 2 loops; in functional programming, we use functions & recursive functions

  // We use flatMap because for each element in the list, we create a list
  val combinations = numbers.flatMap(n => chars.map(c => "" + c + n))
  println(combinations) //>> List(a1, b1, c1, d1, a2, b2, c2, d2, a3, b3, c3, d3, a4, b4, c4, d4)

  // foreach
  list.foreach(println) // print out 1 2 3 on separate lines

  // for-comprehensions
  val colors = List("black", "white")
  val combinations2 = numbers.flatMap(n => chars.flatMap(c => colors.map(color => "" + c + n + "-" + color)))
  println(combinations2)  //>> List(a1-black, a1-white, b1-black, b1-white,...)

  // However, this is not easy to read. We need to use for-comprehensions.
  val forCombinations = for{
    n <- numbers if n % 2 == 0  // you can put a guard here
    c <- chars
    color <- colors
  } yield "" + c + n + "-" + color
  println(forCombinations)

  for {
    n <- numbers
  } println(n)  // identical to .foreach(println)

  // syntax overload
  list.map { x =>
    x * 2
  }

  /*
    1.  MyList supports for comprehensions?
        map(f: A => B) => MyList[B]
        filter(p: A => Boolean) => MyList[A]
        flatMap(f: A => MyList[B]) => MyList[B]
    2.  A small collection of at most ONE element - Maybe[+T]
        - map, flatMap, filter
   */

  // First, we check the function signatures, and it seems to be the same as above
  val combinations3 = for {
    n <- L28HOFsCurriesExercise.listOfIntegers
    string <- L28HOFsCurriesExercise.listOfStrings
  } yield n + "-" + string
  println(combinations3)  //>> [1-Hello 1-Scala 2-Hello 2-Scala 3-Hello 3-Scala]
  // The first one works!
  // If you want to implement your own collection, which supports for-comprehension, you should implement map, flatMap,
  // and filter exactly like MyList interface

  abstract class Maybe[+T] {
    def map[B](f: T => B): Maybe[B]
    def flatMap[B](f: T => Maybe[B]): Maybe[B]
    def filter(p: T => Boolean): Maybe[T]
  }

  case object MaybeNot extends Maybe[Nothing] {
    def map[B](f: Nothing => B): Maybe[B] = MaybeNot
    def flatMap[B](f: Nothing => Maybe[B]): Maybe[B] = MaybeNot
    def filter(p: Nothing => Boolean): Maybe[Nothing] = MaybeNot
  }

  case class Just[+T](value: T) extends Maybe[T] {
    def map[B](f: T => B): Maybe[B] = Just(f(value))
    def flatMap[B](f: T => Maybe[B]): Maybe[B] = f(value)
    def filter(p: T => Boolean): Maybe[T] =
      if (p(value)) this
      else MaybeNot
  }

  val just3 = Just(3)
  println(just3)                                  //>> Just(3)
  println(just3.map(_ * 2))                       //>> Just(6)
  println(just3.flatMap(x => Just(x % 2 == 0)))   //>> Just(false)
  println(just3.filter(_ % 2 == 0))               //>> MaybeNot

}
