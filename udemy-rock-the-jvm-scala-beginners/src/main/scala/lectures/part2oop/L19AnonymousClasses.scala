package lectures.part2oop

object L19AnonymousClasses extends App {

  abstract class Animal {
    def eat: Unit
  }

  // Anonymous Class
  val funnyAnimal: Animal = new Animal {
    override def eat: Unit = println("hahaha")
  }

  class Person(name: String) {
    def sayHi: Unit = println(s"Hi, my name is $name, how can I help?")
  }

  // Anonymous class can work with non-abstract data types
  val jim = new Person("Jim") {
    override def sayHi: Unit = println("Hi, my name is Jim, how can I help?")
  }
}
