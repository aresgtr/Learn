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
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate 2 streams

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
    def ++[B >: Nothing](anotherStream: MyStream[B]): MyStream[B] = anotherStream // concatenate 2 streams

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
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream) // concatenate 2 streams

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
  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList()) // fail with StackOverflow, will fix in next lecture
}