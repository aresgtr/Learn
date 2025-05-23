package lectures.part3fp

import scala.util.{Failure, Random, Success, Try}


object L35HandlingFailure extends App {

  // create success and failure
  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("SUPER FAILURE"))

  println(aSuccess) //>> Success(3)
  println(aFailure) //>> Failure(java.lang.RuntimeException: SUPER FAILURE)

  def unsafeMethod(): String = throw new RuntimeException("NO STRING FOR YOU BUSTER")

  // Try objects via the apply method
  val potentialFailure = Try(unsafeMethod())
  println(potentialFailure) //>> Failure(java.lang.RuntimeException: NO STRING FOR YOU BUSTER)
  // Note that the above line didn't crash, because Try() take care to catch the exception and wrap it up in a Failure
  // if it got caught.

  // syntax sugar
  val anotherPotentialFailure = Try {
    // code that might throw
  }

  // utilities
  println(potentialFailure.isSuccess) //>> false

  // orElse
  def backupMethod(): String = "A valid result"

  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))
  println(fallbackTry)

  // IF you design the API
  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)

  def betterBackupMethod(): Try[String] = Success("A valid result")

  val betterFallback = betterUnsafeMethod() orElse betterBackupMethod()

  // map, flatMap, filter
  println(aSuccess.map(_ * 2)) //>> Success(6)
  println(aSuccess.flatMap(x => Success(x * 10))) //>> Success(30)
  println(aSuccess.filter(_ > 10)) //>> Failure(java.util.NoSuchElementException: Predicate does not hold for 3)

  /*
    Exercise
   */
  val host = "localhost"
  val port = "8080"

  def renderHTML(page: String) = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }

    def getSafe(url: String): Try[String] = Try(get(url))
  }

  object HttpService {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection = {
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the port")
    }

    def getSafeConnection(host: String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  // if you get the html page from the connection, print it to the console i.e. call renderHTML
  val possibleConnection = HttpService.getSafeConnection(host, port)
  val possibleHTML = possibleConnection.flatMap(connection => connection.getSafe("/home"))
  possibleHTML.foreach(renderHTML)

  // shorthand version
  HttpService.getSafeConnection(host, port)
    .flatMap(connection => connection.getSafe("/home"))
    .foreach(renderHTML)

  // for-comprehension version
  for {
    connection <- HttpService.getSafeConnection(host, port)
    htmp <- connection.getSafe("/home")
  } renderHTML(htmp)

  // Equivalent logic in imperative language (it would be a nightmare if you have 10 nested Trys)
  /*
    try {
      connection = HttpService.getConnection(host, port)
      try {
        page = connection.get("/home")
        renderHTML(page)
      } catch (Some other exception) {
      }
    } catch (exception) {
    }
   */
}

/**
 * Wrapping Up
 *
 * Use Try to handle exceptions gracefully:
 * - avoid runtime crashes due to uncaught exceptions
 * - avoid an endless amount of try-catches
 *
 * A functional way of dealing with failure
 * - map, flatMap, filter
 * - orElse
 * - others: fold, collect, toList, conversion to Options
 *
 * If you design a method to return a (some type) but may throw an exception, return a Try[that type] instead.
 */
