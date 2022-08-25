package lectures.part2oop

object OOBasics extends App {
  val person = new Person("Jack", 20)
  person.greet("Zhang")

  val writer = new Writer("Jack", "Ma", 10)
  println(writer.fullname())

  val novel = new Novel("Good Book", 2022, writer)
  println(novel.authorAge())
}

class Person(name: String, val age: Int) {
  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")
}

class Writer(firstname: String, surname: String, val year: Int) {
  def fullname(): String = this.firstname + " " + this.surname
}

class Novel(name: String, year: Int, author: Writer) {
  def authorAge(): Int = year - author.year
}