package lectures.part4implicits

object L33OrganizingImplicits extends App {

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // You can't have two. The compiler doesn't know which implicit value to use.
  //  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)

  // takes an implicit ordering from scala.Predef. However because of above, it will be reverse ordered.
  println(List(1, 4, 5, 3, 2).sorted) //>> List(5, 4, 3, 2, 1)

  /*
    Implicits (used as implicit parameters):
      - val/var
      - object
      - accessor methods = defs with no parentheses
          implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
   */

  // Exercise - implement an implicit ordering for Person, ordered by name
  case class Person(name: String, age: Int)

  val person = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  println(person.sorted) //>> List(Person(Amy,22), Person(John,66), Person(Steve,30))

  /*
    Implicit Scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companions of all types involved in the method signature
      def sorted[B >: A](implicit ord: Ordering[B]): List[B]
      - List
      - Ordering
      - all the types involved = A or any supertype
   */

  /**
   * Best Practices
   *
   * When defining an implicit val:
   * 1.
   * If there is a single possible value for it
   * and you can edit the code for the type
   * Then define the implicit in the companion
   *
   * 2.
   * If there are many possible values for it
   * but a single good one
   * and you can edit the code for the type
   * Then define the good implicit in the companion
   */
}

object ImplicitExample1 extends App {

  case class Person(name: String, age: Int)

  object Person {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)

  val person = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  println(person.sorted) //>> List(Person(Amy,22), Person(Steve,30), Person(John,66))
  // local scope (age ordering) more prioritized
}

object ImplicitExample2 extends App {

  case class Person(name: String, age: Int)

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  val person = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  import AgeOrdering._

  println(person.sorted) //>> List(Person(Amy,22), Person(Steve,30), Person(John,66))

  /*
    Exercise - online store backend and order purchases
      - totalPrice = most used (50%)
      - by unit count = 25%
      - by unit price = 25%
   */
  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }
}


