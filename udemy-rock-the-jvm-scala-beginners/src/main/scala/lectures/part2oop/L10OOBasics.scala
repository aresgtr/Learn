package lectures.part2oop

object OOBasics extends App {
  val person = new Person("John", 26)
//  println(person.name)  // name is a class parameter, not a class member
  // class parameters are NOT FIELDS
  println(person.age) //>> 26 // age is a field, converted by val keyword
  println(person.x) //>> 2

  person.greet("Daniel")  //>> John says: Hi, Daniel
  person.greet()  //>> Hi, I am John

  val author = new Writer("Charles", "Dickens", 1812)
  val novel = new Novel("Great Expectations", 1861, author)

  println(novel.authorAge())  //>> 49
  println(novel.isWrittenBy(author))  //>> true
}

class Person(name: String, val age: Int = 0) {  // constructor
  // body
  val x = 2
  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")

  // overloading
  def greet(): Unit = println(s"Hi, I am $name")

  // multiple constructors
  def this(name: String) = this(name, 0)  // 基本不用了，不如constructor default parameter
}

class Writer(firstname: String, surname: String, val year: Int) {
  def fullname(): String = firstname + " " + surname
}

class Novel(name: String, year: Int, author: Writer) {
  def authorAge(): Int = year - author.year
  def isWrittenBy(author: Writer) = author == this.author
  def copy(newYear: Int): Novel = new Novel(name, newYear, author)
}

/*
  Counter class
    - receives an int value
    - method current count
    - method to increment/decrement => new Counter
    - overload inc/dec to receive an amount
 */
class Counter(val count: Int) {
  def inc = new Counter(count + 1)  // immutability - instances are fixed, cannot be modified inside. Whenever you need
                                    // to modify the contents of an instance, you need to return a new instance.
  def dec = new Counter(count - 1)

  /*
    def inc(n: Int) = new Counter(count + n)
    def dec(n: Int) = new Counter(count - n)
   */
  // recursion
  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n - 1)
  }

  def dec(n: Int): Counter = {
    if (n <= 0) this
    else dec.dec(n - 1)
  }

  def print = println(count)
}