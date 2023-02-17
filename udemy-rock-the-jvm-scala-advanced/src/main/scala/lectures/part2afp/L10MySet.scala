package lectures.part2afp

import scala.annotation.tailrec

/**
 * A Functional Collection: Set
 *
 * A quick warm up: Set is a collection of elements that do not have duplicates
 * val set = Set(1, 2, 3)
 *
 * Set instances are callable (they have apply)
 *    set(2)  // true
 *    set(42) // false
 * Set instances are callable like functions
 * The apply method always returns a value: true/false
 *
 * Sets ARE functions!
 *    trait Set[A] extends (A) => Boolean with...
 */

object L10MySet extends App {

  trait MySet[A] extends (A => Boolean) {
    /*
      Exercise - implement a functional set
     */
    def apply(elem: A): Boolean =
      contains(elem)

    def contains(elem: A): Boolean
    def +(elem: A): MySet[A]
    def ++(anotherSet: MySet[A]): MySet[A]

    def map[B](f: A => B): MySet[B]
    def flatMap[B](f: A => MySet[B]): MySet[B]
    def filter(predicate: A => Boolean): MySet[A]
    def foreach(f: A => Unit): Unit
  }

  // Cost based approach - singly linked set
  // Reason not a singleton object extends MySet[Nothing]: MySet is not covariant. It is invariant.
  class EmptySet[A] extends MySet[A] {
    def contains(elem: A): Boolean = false
    def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
    def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

    def map[B](f: A => B): MySet[B] = new EmptySet[B]
    def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
    def filter(predicate: A => Boolean): MySet[A] = this
    def foreach(f: A => Unit): Unit = ()  // for unit
  }

  class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
    def contains(elem: A): Boolean =
      elem == head || tail.contains(elem)

    def +(elem: A): MySet[A] =
      if (this contains elem) this
      else new NonEmptySet[A](elem, this)

    /*
      [1 2 3] ++ [4 5] =
      [2 3] ++ [4 5] + 1 =
      [3] ++ [4 5] + 1 + 2 =
      [] ++ [4 5] + 1 + 2 + 3 =
      [4 5] + 1 + 2 + 3 = [4 5 1 2 3]
     */
    def ++(anotherSet: MySet[A]): MySet[A] =
      tail ++ anotherSet + head

    def map[B](f: A => B): MySet[B] = (tail map f) + f(head)
    def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)

    def filter(predicate: A => Boolean): MySet[A] = {
      val filteredTail = tail filter predicate
      if (predicate(head)) filteredTail + head
      else filteredTail
    }

    def foreach(f: A => Unit): Unit = {
      f(head)
      tail foreach f
    }
  }

  // Companion object just for convenience of building sets
  object MySet {
    /*
      val s = MySet(1, 2, 3) = buildSet(seq(1, 2, 3), [])
      = buildSet(seq(2, 3), [] + 1)
      = buildSet(seq(3), [1] + 2)
      = buildSet(seq(), [1, 2] + 3)
      = [1, 2, 3]
     */
    def apply[A](values: A*): MySet[A] = {
      @tailrec
      def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
        if (valSeq.isEmpty) acc
        else buildSet(valSeq.tail, acc + valSeq.head)

      buildSet(values.toSeq, new EmptySet[A])
    }
  }


  // Playground (test our code)
  val s = MySet(1, 2, 3, 4)
  s foreach println // print 4 3 2 1 with new lines, we don't care about order
  s + 5 foreach println // print 5 4 3 2 1 with new lines
  s + 5 ++ MySet(-1, -2) foreach println  // print 5 4 3 2 1 -2 -1 with new lines
  s + 5 ++ MySet(-1, -2) + 3 foreach println // 3 should not appear twice

  s map(x => x * 10) foreach println  // print 40 30 20 10 with new lines
  s flatMap(x => MySet(x, 10 * x)) foreach println // print 10 1 20 2 30 3 40 4 with new lines
  s filter(_ % 2 == 0) foreach println  // only prints 4 and 2
}

