package lectures.part4implicits

object L36PimpMyLibrary extends App {

  /**
   * Enrich existing types with Implicits
   *
   * 2.isPrime - Isn't that cool?
   */

  // Implicit class must take 1 and only argument
  implicit class RichInt(val value: Int) {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    // continue with exercise
    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  new RichInt(42).sqrt  // this is good, and we can also say
  42.isEven   // this is super cool, called TYPE ENRICHMENT (Pimping)

  /**
   * Examples
   */
  // That is why we can write
  1 to 10

  import scala.concurrent.duration._
  3.seconds

  /** Compiler doesn't do multiple implicit searches. */
  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }
//  42.isOdd  // Not work, because the complier can wrap 42 into RichInt, but cannot further wrap RichInt into RicherInt.

  /*
    Exercise

    Enrich the String class
    - asInt
    - encrypt
      "John" -> Lqjp

    Keep enriching the Int class
    - times(function)
      3.times(() => ...)
    - *
      3 * List(1,2) -> List(1,2,1,2,1,2)
   */

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string)
    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)  //>> 7
  println("John".encrypt(2))  //>> Lqjp

  3.times(() => println("Scala Rocks!"))
  //>> Scala Rocks!
  //>> Scala Rocks!
  //>> Scala Rocks!

  println(4 * List(1, 2)) //>> List(1, 2, 1, 2, 1, 2, 1, 2)

  /**
   * "3" / 4 - Isn't that cool?
   */
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2)  //>> 3  // stringToInt("6") / 2

  /**
   * Implicit classes are plain classes with an implicit conversion.
   *
   * Below is equivalent to:
   * implicit class RichAltInt(value: Int)
   */
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  /**
   * Although the implicit conversions with methods are more powerful, they are discouraged.
   * If there is a bug in an implicit def, it's super hard to trace it back.
   *
   * The danger zone example:
   */
  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionedValue = if (3) "OK" else "Something wrong"
  println(aConditionedValue)  //>> Something wrong
}

/**
 * Pimp Libraries Responsibly
 *
 * Tips:
 * - keep type enrichment to implicit classes and type classes
 * - avoid implicit defs as much as possible
 * - package implicits clearly, bring into scope only what you need
 * - IF you need conversions, make them specific (never with general types)
 */
