package lectures.part2afp

import scala.annotation.tailrec

object L16LazyEvaluationExercise extends App {
  /**
   * Implement a lazily evaluated, singly linked STREAM of elements.
   *
   * naturals = MyStream.from(1)(x => x + 1)  // stream of natural numbers (potentially infinite!)
   * naturals.take(100).foreach(println)  // lazily evaluated stream of the first 100 naturals (finite stream)
   * naturals.foreach(println)  // will crash - infinite!
   * naturals.map(_ * 2)  // stream of all even numbers (potentially infinite)
   */

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B]  // prepend operator
    def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // concatenate 2 streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of this steam
    def takeAsList(n: Int): List[A] = take(n).toList()

    /*
      [1 2 3].toList([]) =
      [2 3].toList([1]) =
      [3].toList([2 1]) =
      [].toList([3 2 1])    // reverse
      = [1 2 3]
     */
    @tailrec
    final def toList[B >: A](acc: List[B] = Nil): List[B] = // auxiliary function, final prevents override
      if (isEmpty) acc.reverse
      else tail.toList(head :: acc)
  }

  object EmptyStream extends MyStream[Nothing] {
    def isEmpty: Boolean = true
    def head: Nothing = throw new NoSuchElementException
    def tail: MyStream[Nothing] = throw new NoSuchElementException

    def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)  // prepend operator
    def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream // concatenate 2 streams

    def foreach(f: Nothing => Unit): Unit = ()
    def map[B](f: Nothing => B): MyStream[B] = this
    def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
    def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

    def take(n: Int): MyStream[Nothing] = this // takes the first n elements out of this steam
  }

  class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] { // notice the tail Call-by-Name
    def isEmpty: Boolean = false
    override val head: A = hd // We override it to val, since we will use (and reuse) it throughout implementations
    override lazy val tail: MyStream[A] = tl  // Call-by-Need: combining a Call-by-Name parameter with a lazy val

    /*
      val s = new Cons(1, EmptyStream)        // Whatever is at the tail of s will still remain unevaluated
      val prepend = 1 #:: s = new Cons(1, s)  // when the prepend operator acts.
     */
    def #::[B >: A](element: B): MyStream[B] = new Cons(element, this)  // prepend operator
    def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream) // concatenate 2 streams

    def foreach(f: A => Unit): Unit = {
      f(head)
      tail.foreach(f)
    }

    /*
      s = new Cons(1, ?)
      mapped = s.map(_ + 1) = new Cons(2, s.tail.map(_ + 1))
        ... mapped.tail
     */
    def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))
    def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)
    def filter(predicate: A => Boolean): MyStream[A] =
      if (predicate(head)) new Cons(head, tail.filter(predicate))
      else tail.filter(predicate) // preserves lazy eval!

    def take(n: Int): MyStream[A] = { // takes the first n elements out of this steam
      if (n <= 0) EmptyStream
      else if (n == 1) new Cons(head, EmptyStream)
      else new Cons(head, tail.take(n - 1))
    }

  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] =
      new Cons(start, MyStream.from(generator(start))(generator))
  }

  // Playground
  val naturals = MyStream.from(1)(_ + 1)  // should not crash our code, because the latter part is lazy evaluated
  println(naturals.head)  //>> 1
  println(naturals.tail.head) //>> 2
  println(naturals.tail.tail.head)  //>> 3  (is only evaluated when we need it)

  val startFrom0 = 0 #:: naturals // behind the scene: actually rewritten to naturals.#::(0)
  println(startFrom0.head)  //>> 0

  startFrom0.take(10000).foreach(println) // println from 0 to 9999, no crash

  // map, flatMap
  println(startFrom0.map(_ * 2).take(10).toList())  //>> List(0, 2, 4, 6, 8, 10, 12, 14, 16, 18)

  // This could potentially fail with StackOverflow. To fix it, the ++ operator needs Call-by-Name.
  // So that we can delay the evaluation of tail.flatMap(f) until needed.
  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList()) //>> List(0, 1, 1, 2, 2, 3, 3, 4, 4, 5)

  println(startFrom0.filter(_ < 10).take(10).toList())  //>> List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

  /*
    Exercises on steams
    1 - stream of Fibonacci numbers
    2 - stream of PRIME numbers with Eratosthenes' sieve
      [ 2 3 4 ... ]
      filter out all numbers divisible by 2
      [ 2 3 5 7 9 11 ...] keep 2, since 2 is prime
      filter out all numbers divisible by 3
      [ 2 3 5 7 11 13 17 ...]
      filter out all numbers divisible by the next number, 5
      ...
   */

  /*
    [ first, [ ...
    [ first, fibo(second, first + second)
   */
  def fibonacci(first: Int, second: Int) : MyStream[Int] =
    new Cons(first, fibonacci(second, first + second))

  println(fibonacci(1, 1).take(10).toList())  //>> List(1, 1, 2, 3, 5, 8, 13, 21, 34, 55)

  /*
    [ 2 3 4 5 6 7 8 9 10 11 12 ...
    [ 2 3 5 7 9 11 13 ...
    [ 2 eratosthenes applied to (numbers filtered by n % 2 != 0)
    [ 2 3 eratosthenes applied to [ 5 7 9 11 ... ] filtered by n % 3 != 0
   */
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter( n => n % numbers.head != 0)))

  // MyStream of all the natural numbers from 2
  println(eratosthenes(MyStream.from(2)(_ + 1)).take(10).toList())  //>> List(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
}