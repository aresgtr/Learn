package lectures.part2oop

import playground.Cinderella // alias

object L24PackagingAndImports extends App {

  // Package members are accessible by their simple name. (from L10OOBasics)
  val writer = new Writer("Daniel", "RockTheJVM", 2018)

  // Import the package
  val princess = new Cinderella
  val princess2 = new playground.Cinderella // fully qualified name

  // Packages are in hierarchy, matching folder structure.

  // Package object - refer to part2oop\package.scala
  sayHello  //>> Hello, Scala
  println(SPEED_OF_LIGHT) //>> 299792458

  // Imports
  // If you have two Dates
  import java.util.Date
  import java.sql.{Date => SqlDate} // alias
  // option 1: use fully qualified name
  // option 2: use alias
  val date = new Date
  val sqlDate = new SqlDate(2018, 5, 4)

  // Default imports - packages that are automatically imported without any intentional import
  // java.lang - String, Object, Exception
  // scala - Int, Nothing, Function
  // scala.Predef - println, ???
}
