package lectures.part5ts

object L46TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???

//  val cat: ac.BoundedAnimal = new Cat // not allowed

  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  // type member also work outside
  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // 💡 Type members are used in APIs alternative to generics
  // for example
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    def add(element:  Int): MyList = ???
  }

  // 💡 .type
  type CatsType = cat.type
  val newCat: CatsType = cat
//  new CatsType  // invalid


  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
   */
  // LOCKED, wrote by other teams
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  // For some reason, you consider this stupid
  // NOT OK, but still compiles
  class CustomList(hd: String, t1: CustomList) extends MList {
    type A = String
    def head = hd
    def tail = t1
  }

  // OK
  class IntList(hd: Int, t1: IntList) extends MList {
    type A = Int
    def head = hd
    def tail = t1
  }

  // Number
  // type members and type member constrains (bounds)

  // Solution
  trait ApplicableToNumbers {
    type A <: Number
  }

  // And then CustomList and IntList should extends this trait as well. This is a safe guard at compile time.
}
