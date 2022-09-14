package lectures.part3fp

import scala.annotation.tailrec

object L27HOFsCurries extends App {

  // In the last videos you saw function types. What keeps us from defining such a function? Nothing.
  // But how do you read such a function? What kind of parameters does this function have and what is the return type of
  // this function?

  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = ???
  //                                                Return type: (Int => Int)
  //     Parameters: (Int, (String, (Int => Boolean)) => Int)

  // So the super function takes two parameters, an Int and a function and returns another function.
  // Such a function that either takes function as parameters or returns functions as a result is called a
  // Higher Order Function (HOF)

  // Example of HOF:
  // map, flatMap, filter in MyList

  // Function that applies a function n times over a value of x
  // nTimes(f, n, x)
  // nTimes(f, 3, x) = f(f(f(x)))
  // nTimes(f, n, x) = f(f(...f(x))) = nTimes(f, n-1, f(x))
  // functional programming is a direct mapping of mathematics

  @tailrec
  def nTimes(f: Int => Int, n: Int, x: Int): Int = {
    if (n <= 0) x
    else nTimes(f, n-1, f(x))
  }

  val plusOne = (x:Int) => x + 1
  println(nTimes(plusOne, 10, 0))   //>> 10

  // A better approach:
  // Instead of passing all parameters at once, the function takes a function and a number, and returns another function,
  // which is the application of this function n times, and then use it on any value you want.

  // ntb(f, n) = x => f(f(f...(x)))
  // increment10 = ntb(plusOne, 10) = x => plueOne(plueOne...(x))
  // val y = increment10(1)
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) = {
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n-1)(f(x)) // nTimesBetter will return a function, that will applied to f(x)
  }

  val plusTen = nTimesBetter(plusOne, 10)
  println(plusTen(0))   //>> 10

  // Curried Functions - useful when you want to define helper functions that will be used later on
  val superAdder: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
  val add3 = superAdder(3)  // y => 3 + y
  println(add3(10))   //>> 13

  // Curried Functions = Functions with multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  def standardFormat: (Double => String) = curriedFormatter("%4.2f")  // you have to specify the type
  def preciseFormat: (Double => String) = curriedFormatter("%10.8f")

  println(standardFormat(Math.PI))  //>> 3.14
  println(preciseFormat(Math.PI))   //>> 3.14159265
}
