package lectures.part2oop

object L23Exceptions extends App {

  val x: String = null
//  println(x.length) // this will cash with NullPointerException

  // 1. Throwing exceptions
//  val aWeirdValue: String = throw new NullPointerException // valid, type Nothing

  /*
    Throwable classes extend the Throwable class, and Exception and Error are the major Throwable subtypes.
    Exceptions denotes something that went wrong with the program, e.g. NullPointerException
    Error denotes something wrong with the system, e.g. StackOverflowError (exceed memory of JVM stack)
   */

  // 2. Catching exceptions
  def getInt(withExceptions: Boolean): Int =
    if (withExceptions) throw new RuntimeException("No int for you!")
    else 42

  val potentialFail = try { // you can assign val to the try-catch-finally block, type Int
    // code that might fail
    getInt(true)
  } catch {
    case e: RuntimeException => 43
  } finally {
    // Code that will get executed NO MATTER WHAT
    // Optional, does not influence the return type of this expression, use finally only for side effects.
    println("finally")
  }

  println(potentialFail)  //>> 43

  // 3. Define your own exception
  class MyException extends Exception
  val exception = new MyException
//  throw exception

  /*
    Exercise
    1. Crash your program with an OutOfMemoryError
    2. Crash with StackOverflowError
    3. PocketCalculator
      - add(x, y)
      - subtract(x, y)
      - multiply(x, y)
      - divide(x, y)
      Throw
        - OverflowException if add(x, y) exceeds Int.MAX_VALUE
        - UnderflowException if subtract(x, y) exceeds Int.MIN_VALUE
        - MathCalculationException for division by 0
   */

  // OOM
//  val array = Array.ofDim[Int](Int.MaxValue)  // crash with OutOfMemoryError
  // SO
//  def infinite: Int = 1 + infinite
//  val noLimit = infinite  // crash with StackOverflowError

  class OverflowException extends RuntimeException
  class UnderflowException extends RuntimeException
  class MathCalculationException extends RuntimeException("Division by 0")

  object PocketCalculator {
    def add(x: Int, y: Int) = {
      val result = x + y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && result > 0) throw new UnderflowException
      else result
    }

    def subtract(x: Int, y: Int) = {
      val result = x - y
      if (x > 0 && y < 0 && result < 0) throw new OverflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException
      else result
    }

    def multiply(x: Int, y: Int) = {
      val result = x * y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && result < 0) throw new OverflowException
      else if (x > 0 && y < 0 && result > 0) throw new UnderflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException
      result
    }

    def divide(x: Int, y: Int) = {
      if (y == 0) throw new MathCalculationException
      else x / y
    }
  }
}
