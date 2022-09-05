package lectures.part2oop

object L20MyList extends App {

  /*
    1.  Generic trait MyPredicate[-T] with a little method test(T) => Boolean
    2.  Generic trait MyTransformer[-A, B] with a method transform(A) => B
    3.  MyList:
        - map(transformer) => MyList
        - filter(predicate)=> MyList
        - flatMap(transformer from A to MyList[B]) => MyList[B]

        class EvenPredicate extends MyPredicate[Int]
        class StringToIntTransformer extends MyTransformer[String, Int]

        [1, 2, 3].map(n * 2) = [2, 4, 6]
        [1, 2, 3, 4].filter(n % 2) = [2, 4]
        [1, 2, 3].flatMap(n => [n, n + 1]) = [1, 2, 2, 3, 3, 4]
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

  object Empty extends MyList[Nothing] {
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

  class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
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
}
