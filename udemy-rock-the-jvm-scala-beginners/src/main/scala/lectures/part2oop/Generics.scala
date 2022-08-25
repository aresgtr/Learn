package lectures.part2oop

object Generics extends App {
  class MyList[A] {

  }

  val listOfIntegers = new MyList[Int]

  object MyList {
    def empty[A]: MyList[A] = ???
  }

  val emptyListOfInt = MyList.empty[Int]
}
