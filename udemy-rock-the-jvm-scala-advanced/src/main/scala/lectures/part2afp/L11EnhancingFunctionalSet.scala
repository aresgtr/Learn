package lectures.part2afp

import scala.annotation.tailrec

class L11EnhancingFunctionalSet extends App {

  trait MySet[A] extends (A => Boolean) {
    /*
      Exercise - implement a functional set
     */
    def apply(elem: A): Boolean =
      contains(elem)

    def contains(elem: A): Boolean
    def +(elem: A): MySet[A]
    def ++(anotherSet: MySet[A]): MySet[A]  // union

    def map[B](f: A => B): MySet[B]
    def flatMap[B](f: A => MySet[B]): MySet[B]
    def filter(predicate: A => Boolean): MySet[A]
    def foreach(f: A => Unit): Unit

    /*
      EXERCISE #2
      - removing an element
      - intersection with another set
      - difference with another set
     */
    def -(elem: A): MySet[A]
    def --(anotherSet: MySet[A]): MySet[A]  // difference
    def &(anotherSet: MySet[A]): MySet[A]   // intersection

    /*
      EXERCIESE #3 - implement a unary_! = NEGATION of a set
      - if we have Set[1, 2, 3], unary_! will give a set with everything EXCEPT 1, 2, 3
     */
    def unary_! : MySet[A]

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

    // part 2
    def -(elem: A): MySet[A] = this
    def --(anotherSet: MySet[A]): MySet[A] = this
    def &(anotherSet: MySet[A]): MySet[A] = this

    // part 3
    def unary_! : MySet[A] = new AllInclusiveSet[A]
  }

  // Will be replaced by PropertyBasedSet
  class AllInclusiveSet[A] extends MySet[A] {
    override def contains(elem: A): Boolean = true
    override def +(elem: A): MySet[A] = this
    override def ++(anotherSet: MySet[A]): MySet[A] = this

    // naturals = AllInclusiveSet[Int] = all the natural numbers
    // naturals.map(x => x % 3) => ???
    // The answer is [0 1 2]
    override def map[B](f: A => B): MySet[B] = ???
    override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
    override def foreach(f: A => Unit): Unit = ???

    // idea of Property-Based Set
    // means, all the elements of Type A that satisfy this predicate, which could be an infinite number of those elements
    // However, this does not make sense here, so we create the class PropertyBasedSet
    override def filter(predicate: A => Boolean): MySet[A] = ???

    override def -(elem: A): MySet[A] = ???
    override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
    override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

    override def unary_! : MySet[A] = new EmptySet[A]
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

    // part 2
    def -(elem: A): MySet[A] =
      if (head == elem) tail
      else tail - elem + head // recursively calling the method and then add the head back

//    def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet.contains(x))
//    def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))
    // The above 2 lines are equivalent. We can even use unary operator to make it nicer.


//    def &(anotherSet: MySet[A]): MySet[A] = filter(x => anotherSet.contains(x))
    def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // equivalent to above. intersection = filtering!
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
}
