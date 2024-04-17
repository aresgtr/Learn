package lectures.part5ts

object L49FBoundedPolymorphism extends App {

  /* A little design problem:
     - I have a class hierarchy
     - How do I force a method in the super type to accept a "current type"?
     - example:

  trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Animal] = ???  // we want List[Cat] !!
  }

  class Dog extends Animal {
    override def breed: List[Animal] = ???  // we want List[Dog] !!
  }
   */

  // Solution 1 - naive
  {
    trait Animal {
      def breed: List[Animal]
    }

    class Cat extends Animal {
      override def breed: List[Cat] = ???
    }

    class Dog extends Animal {
      override def breed: List[Cat] = ??? // we want List[Dog] !!
    }
  }


  // Solution 2
  {
    trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Animal[Cat]] = ???
    }

    class Dog extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = ???
    }

    // Real life example
    trait Entity[E <: Entity[E]] // ORM Framework - database APIs
    class Person extends Comparable[Person] {
      override def compareTo(o: Person): Int = ??? // this is also F-Bounded Polymorphism
    }

    // Limitations: human can make mistakes
    class Crocodile extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = ??? // we want List[Dog] !!
    }
  }


  // Solution 3 - F-Bounded Poly + Self Types (to force the same type)
  {
    trait Animal[A <: Animal[A]] {
      self: A =>
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Animal[Cat]] = ???
    }

    class Dog extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = ???
    }

    //  class Crocodile extends Animal[Dog] {   // Not compilable, GOOD
    //    override def breed: List[Animal[Dog]] = ???
    //  }

    // Limitations of F-Bounded Poly
    trait Fish extends Animal[Fish]
    class Cod extends Fish {
      override def breed: List[Animal[Fish]] = ???
    }

    class Shark extends Fish {
      override def breed: List[Animal[Fish]] = List(new Cod) // ❎
    }
  }


  // To solve 👆 problem
  // Solution 4 - Type Classes!
  {
    trait Animal
    trait CanBreed[A] { // We implement Type Class Pattern by: 1️⃣ defining the type class's methods
      def breed(a: A): List[A] // Here we enforce the type.
    }

    class Dog extends Animal
    object Dog {
      implicit object DogsCanBreed extends CanBreed[Dog] { // 2️⃣ define type class instances as implicit objects/values
        override def breed(a: Dog): List[Dog] = List()
      }
    }

    implicit class CanBreedsOps[A](animal: A) {
      def breed(implicit canBreed: CanBreed[A]): List[A] =
        canBreed.breed(animal)
    }

    val dog = new Dog // 3️⃣ convert little object (type Dog) into ↖️ something can access to type class instance with method "breed"
    dog.breed
    /*
    new CanBreedOps[Dog](dog).breed
    implicit value to pass to breed: Dog.DogsCanBreed
   */

    // How the compiler signal human mistakes?
    class Cat extends Animal
    object Cat {
      implicit object CatsCanBreed extends CanBreed[Dog] {
        def breed(a: Dog): List[Dog] = List()
      }
    }

    val cat = new Cat
    cat.breed // illegal, which is GOOD

    // Small criticism: it splits the API between the trait animal and the abstract CanBreed trait
  }


  // Solution 5 - trait Animal being the type class itself
  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed
}
