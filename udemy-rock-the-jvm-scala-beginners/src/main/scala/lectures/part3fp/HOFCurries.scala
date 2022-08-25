package lectures.part3fp

import scala.annotation.tailrec

object HOFCurries extends App {
  // higher order function (HOF)
  // map, flatMap, filter in MyList

  // function that applies a function n times over a value of x
  // nTimes(f, n, x)
  // nTimes(f, 3, x) = f(f(f(x)))
  // nTimes(f, n, x) = f(f(...f(x))) = nTimes(f, n-1, f(x))
  @tailrec
  def nTimes(f: Int => Int, n: Int, x: Int): Int = {
    if (n <= 0) x
    else nTimes(f, n-1, f(x))
  }
  val plusOne = (x:Int) => x + 1
  println(nTimes(plusOne, 10, 0))

  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) = {
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n-1)(f(x))
  }
  val plusTen = nTimesBetter(plusOne, 10)
  println(plusTen(0))
}
