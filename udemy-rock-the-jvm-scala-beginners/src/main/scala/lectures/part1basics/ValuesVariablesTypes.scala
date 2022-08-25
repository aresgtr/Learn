package lectures.part1basics

object ValuesVariablesTypes extends App {
  val x = 42
  println(x)

  def factorial(n: Int, accumulator: Int = 1): Int = {
    if (n <= 1) accumulator
    else factorial(n - 1, n * accumulator)
  }

  println(factorial(10))

  var multTable = Array.ofDim[Int](10, 10)

  for (i <- 0 to 9) {
    for (j <- 0 to 9) {
      multTable(i)(j) = i * j
    }
  }
  for (i <- 0 to 9) {
    for (j <- 0 to 9) {
      printf("%d : %d = %d\n", i, j, multTable(i)(j))
    }
  }

}
