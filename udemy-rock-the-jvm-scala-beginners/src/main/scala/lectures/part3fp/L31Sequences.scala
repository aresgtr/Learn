package lectures.part3fp

import scala.util.Random

object L31Sequences extends App {

  /**
   * Sequences
   *
   * trait Seq[+A] {
   *   def head: A
   *   def tail: Seq[A]
   * }
   *
   * A (very) general interface for data structures that
   * - have a well defined order
   * - can be indexed
   *
   * Supports various operations:
   * - apply, iterator, length, reverse for indexing and iterating
   * - concatenation, appending, prepending
   * - a lot of others: grouping, sorting, zipping, searching, slicing
   */

  // Seq
  val aSequence = Seq(1, 2, 3, 4)
  println(aSequence)  //>> List(1, 2, 3, 4)
  println(aSequence.reverse)
  println(aSequence(2)) // at index #2
  println(aSequence ++ Seq(5, 6, 7))  // concatenation
  println(aSequence.sorted)

  // Ranges
  val aRange: Seq[Int] = 1 to 10
  aRange.foreach(println) // print all numbers from 1 to 10

  (1 to 10).foreach(x => println("Hello")) // print hello 10 times

  /**
   * List
   *
   * A list is immutable, extends LinearSeq
   * - head, tail, isEmpty methods are fast: O(1)
   * - most operations are O(n): length, reverse
   *
   * Sealed, has two subtypes:
   * - object Nil (empty)
   * - class ::
   */

  // Lists
  val aList = List(1, 2, 3)
  val prepended = 42 :: aList // you can also use +: for prepending
  println(prepended)  //>> List(42, 1, 2, 3)

  val prepended2 = 42 +: aList :+ 89
  println(prepended2) //>> List(42, 1, 2, 3, 89)

  val apples5 = List.fill(5)("apple")
  println(apples5) //>> List(apple, apple, apple, apple, apple)

  println(aList.mkString("-|-"))  //>> 1-|-2-|-3

  /**
   * Array
   *
   * The equivalent of simple Java arrays
   * - can be manually constructed with predefined lengths
   * - can be mutated (updated in place)
   * - are interoperable with Java's T[] arrays
   * - indexing is fast
   *
   * Where is the Seq??
   */

  // Arrays
  val numbers = Array(1, 2, 3, 4)
  val threeElements = Array.ofDim[Int](3) // allocates space for three elements without supplying values

  // mutation
  numbers(2) = 0  // update index #2, syntax sugar for numbers.update(2, 0)
  println(numbers.mkString(" "))  //>> 1 2 0 4

  // Arrays and Seq
  val numbersSeq: Seq[Int] = numbers  // Implicit conversion: this conversion can be applied, despite numbers is Array
  println(numbersSeq) //>> ArraySeq(1, 2, 0, 4)

  /**
   * Vector
   *
   * The default implementation for immutable sequences
   * - effectively constant indexed read and write: O(log32(n))
   * - fast element addition: append/prepend
   * - implemented as a fixed-branched trie (branch factor 32) - holds 32 elements at any one level, optimize memory & cache
   * - good performance for large sizes
   */

  // Vectors
  val vector: Vector[Int] = Vector(1, 2, 3)
  println(vector) //>> Vector(1, 2, 3)

  // Vectors vs Lists
  val maxRuns = 1000
  val maxCapacity = 1000000

  def getWriteTime(collection: Seq[Int]): Double = {
    val r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      // operation
      collection.updated(r.nextInt(maxCapacity), r.nextInt())
      System.nanoTime() - currentTime
    }
    times.sum * 1.0 / maxRuns
  }

  val numbersList = (1 to maxCapacity).toList
  val numbersVector = (1 to maxCapacity).toVector

  // Pros: keeps the reference to tails
  // Cons: updating an element in the middle takes long
  println(getWriteTime(numbersList))  //>> 4886057.593

  // Pros: depth of the tree is snall
  // Cons: needs to replace an entire 32-element chunk
  println(getWriteTime(numbersVector))  //>> 7878.673
}
