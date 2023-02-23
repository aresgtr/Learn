package lectures.part2afp

import scala.annotation.tailrec

object L12FunctionalSetLvl9000 extends App {

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
    def unary_! : MySet[A] = new PropertyBasedSet[A](x => true)
  }

  // It is more flexible in defining properties for a potentially infinite functional set.
  // denotes all the elements of Type A satisfy the property
  // { x in A | property(x) } in math terms
  class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
    override def contains(elem: A): Boolean = property(elem)

    // { x in A | property(x) } + element = { x in A | property(x) or x == element}
    override def +(elem: A): MySet[A] =
      new PropertyBasedSet[A](x => property(x) || x == elem)

    // { x in A | property(x) } ++ set = { x in A | property(x) or set contains x }
    override def ++(anotherSet: MySet[A]): MySet[A] = // union
      new PropertyBasedSet[A](x => property(x) || anotherSet(x))

    // cannot really map and flatmap a property-based set
    // all intergers => (_ % 3) => [0 1 2]
    // If you map an infinite set with a function, you will not know whether the resulting set is finite or not.
    // So you won't be able to tell if an element is in the set or not.
    override def map[B](f: A => B): MySet[B] = politelyFail
    override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
    override def foreach(f: A => Unit): Unit = politelyFail

    override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))
    override def -(elem: A): MySet[A] = filter(x => x != elem)
    override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet) // difference
    override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)   // intersection
    override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

    def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
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
    def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

    //    def &(anotherSet: MySet[A]): MySet[A] = filter(x => anotherSet.contains(x))
    def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // equivalent to above. intersection = filtering!

    // new operator
    def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
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

  // Playground
  val s = MySet(1, 2, 3, 4)

  val negative = !s // s.unary_! = all the naturals not equal to 1, 2, 3, 4

  println(negative(2))  //>> false
  println(negative(5))  //>> true

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))  //>> false

  val negativeEven5 = negativeEven + 5  // all the even numbers > 4 + 5
  println(negativeEven5(5)) //>> true
}
