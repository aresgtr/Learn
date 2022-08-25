package lectures.part4implicits

object L32ImplicitsIntro extends App {

  // -> arrows are implicit methods
  val pair = "Daniel" -> "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)  //>> Hi, my name is Peter
  // Although String doesn't have greet() method, the compiler looks for an implicit classes, objects, values, and
  // methods that can help in the compilation.
  // i.e. the compiler looks for anything that can turn this string into something that has greet() method
  /*
    println(fromStringToPerson("Peter").greet)
   */

  // implicit parameters
  def increment(x: Int)(implicit amount: Int): Int = x + amount
  implicit val defaultAmount = 10

  print(increment(2)) //>> 12
  // NOT the same as default args
}
