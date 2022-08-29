package lectures.part2oop

object L14Objects extends App {

  /* Java code
    ...
    public static void main(String args[]) {
        System.out.println(Person.N_EYES);
    }
    ...
    class Person {
        public static final int N_EYES = 2;
    }
   */

  // Scala does not have class-level functionality ("static")
  // Equivalent to above Java code
  object Person {   // objects do not receive parameters
    // "static"/"class"-level functionality
    val N_EYES = 2
    def canFly: Boolean = false

    // factory method
    def apply(mother: Person, father: Person): Person = new Person("Bobbie")
  }

  class Person (val name: String) {
    // instance-level functionality
  }
  // COMPANIONS

  println(Person.N_EYES)  //>> 2
  println(Person.canFly)  //>> false

  // Scala object = SINGLETON INSTANCE
  val mary = Person
  val john = Person
  println(mary == john)   //>> true

  val mary2 = new Person("Mary")  // instantiate
  val john2 = new Person("John")
  println(mary2 == john2) //>> false

  val bobbie = Person(mary2, john2)
}

object test {
  // Scala Applications = Scala object with
   def main(args: Array[String]): Unit = println("Hello world")
}
