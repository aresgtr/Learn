package lectures

package object part2oop {

  /**
   * Problem to solve:
   *  Sometimes we may want to write methods or constants outside everything else.
   *  We might need to have universal constants or universal methods that reside outside classes.
   *
   * A package can only contain one package object with the same name in a file called package.scala
   * Inside the package object, we can define methods or constants and use them by their simple name across the package
   */
  
  def sayHello: Unit = println("Hello, Scala")
  val SPEED_OF_LIGHT = 299792458
}
