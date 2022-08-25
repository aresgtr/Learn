package lectures.part3fp

import scala.util.Random

object L34Options extends App {

  val myFirstOption: Option[Int] = Some(4)
  val noOption: Option[Int] = None

  println(myFirstOption)
  //>> Some(4)

  /**
   * Options were invented to deal with unsafe APIs
   */
  // Suppose we have a function, expected String, but actually returns a null
  def unsafeMethod(): String = null
  //  val result = Some(unsafeMethod())
  // WRONG, you might get Some(null). Some should always have a valid value

  /**
   * We should never do null checks ourselves
   */

  val result = Option(unsafeMethod()) // build Some or None, depending on the value inside
  println(result)
  //>> None

  /**
   * Options should be used in chained methods to work with unsafe APIs
   */
  def backupMethod(): String = "A valid result"

  val chainedResult = Option(unsafeMethod()).orElse(Option(backupMethod()))

  // DESIGN unsafe APIs
  def betterUnsafeMethod(): Option[String] = None

  def betterBackupMethod(): Option[String] = Some("A valid result")

  val betterChainedResult = betterUnsafeMethod() orElse betterBackupMethod() // This is good

  // functions on Options
  println(myFirstOption.isEmpty) // isEmpty tests for options whether they have a value or not
  //>> false
  println(myFirstOption.get) // tries to retrieve the value from option, UNSAFE - DO NOT USE THIS

  // map, flatMap, filter
  println(myFirstOption.map(_ * 2))
  //>> Some(8)
  println(myFirstOption.filter(x => x > 10)) // 4 does not match the predicate
  //>> None
  println(myFirstOption.flatMap(x => Option(x * 10)))
  //>> Some(40)

  // for-comprehensions

  /*
    Exercise.
    Simulates the possibility of a connection or faulty connection
  */
  val config: Map[String, String] = Map(
    "host" -> "176.45.36.1",
    "port" -> "80"
  )

  class Connection {
    def connect = "Connected" // connect to some server
  }

  object Connection {
    val random = new Random(System.nanoTime())

    def apply(host: String, port: String): Option[Connection] =
      if (random.nextBoolean()) Some(new Connection)
      else None
  }

  // try to establish a connection, if so - print the connect method
  val host = config.get("host")
  val port = config.get("port")
  /*
    if (h != null)
      if (p != null)
        return Connection.apply(h, p)

    return null
  */
  val connection = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))
  /*
    if (c != null)
      return c.connect
    return null
  */
  val connectionStatus = connection.map(c => c.connect)
  /*
    if (connectionStatus == null) println(None) else println(Some(connectionStatus.get))
  */
  println(connectionStatus)
  /*
    if (status !- null)
      println(status)
  */
  connectionStatus.foreach(println)

  // A shorter alternative - chained calls
  config.get("host")
    .flatMap(host => config.get("port")
      .flatMap(port => Connection(host, port)
        .map(connection => connection.connect)))
    .foreach(println)

  // for-comprehensions
  val forConnectionStatus = for {
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  } yield connection.connect
  forConnectionStatus.foreach(println)
}

/**
 * Wrapping Up
 *
 * Use Options to stay away from the Boogeyman:
 * - avoid runtime crashes due to NPEs
 * - avoid an endless amount of null-related assertions
 *
 * A functional way of dealing with absence
 * - map, flatMap, filter
 * - orElse
 * - others: fold, collect, toList
 *
 * If you design a method to return a (some type) but may return null, return an Option[that type] instead
 */
