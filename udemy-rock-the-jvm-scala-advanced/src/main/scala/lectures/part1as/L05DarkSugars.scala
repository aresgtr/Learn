package lectures.part1as

import scala.util.Try

object L05DarkSugars extends App {

  // Syntax sugar # 1: methods with single param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod{
    // write some comlex code
    // Instead of calling the method with an argument in (), we can call the method in {} and supply the parameter here
    42
  }

  val aTryInstance = Try {  // java's try {...}
    throw new RuntimeException
  }

  List(1, 2, 3).map { x =>
    x + 1
  }


  // Syntax sugar # 2: single abstract method
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  } // can convert to single abstract method

  // single abstract method: I can reduce an instance of this single method trait to a lambda
  val aFunkyInstance: Action = (x: Int) => x + 1

  // example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hello Scala")
  })

  val aSweeterThread = new Thread(() => println("Sweet, Scala!"))


  // This pattern also works for classes that have some members or methods implemented, but only have one method unimplemented
  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")


  // Syntax sugar # 3: the :: and #::methods are special
  val prependedList = 2 :: List(3, 4)
  // 2.::(List(3, 4))   ??? NO
  // List(3, 4).::(2)   Yes
  // ?!

  // Scala spec: last char decides associativity of method
  1 :: 2 :: 3 :: List(4, 5)
  List(4, 5).::(3).::(2).::(1)  // equivalent

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this  // actual implementation here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int] // This is good because : right associative


  // Syntax sugar # 4: multi-word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"  //>> Lilly said Scala is so sweet!


  // Syntax sugar #5: infix types
  class Composite[A, B]
  val composite: Composite[Int, String] = ???
  val composite2: Int Composite String = ???  // equivalent

  class -->[A, B]
  val towards: Int --> String = ???


  // Syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1, 2, 3)
  anArray(2) = 7  // rewritten to anArray.update(2, 7)
  // used in mutable collections
  // remember apply() AND update()!


  // Syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member = internalMember // "getter"
    def member_=(value: Int): Unit =
      internalMember = value    // "setter"
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_=(42)
}
