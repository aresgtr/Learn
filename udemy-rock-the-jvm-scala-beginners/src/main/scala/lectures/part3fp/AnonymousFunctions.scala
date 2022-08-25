package lectures.part3fp

object AnonymousFunctions extends App {
  val doubler: Int => Int = x => x * 2

  println(doubler(1))
}
