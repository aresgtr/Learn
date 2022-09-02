package lectures.part2oop

import lectures.part4pattern_matching.L36PatternMatching.Dog

object L16AbstractDataTypes extends App {

  // Abstract
  abstract class Animal {
    val creatureType: String
    def eat: Unit
  }

  class Dog extends Animal {
    override val creatureType: String = "Canine"
    override def eat: Unit = println("crunch crunch") // override keyword is not mandatory for abstract members
  }

  // Traits
  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override val creatureType: String = "croc"
    override def eat: Unit = println("nomnomnom")

    override def eat(animal: Animal): Unit = println(s"I'm a croc and I'm eating ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat(dog) //>> I'm a croc and I'm eating Canine

  /**
   * Traits vs Abstract Class
   * Both can have non-abstract members (default value for val and def)
   * Differences
   * 1 - traits do not have constructor parameters
   * 2 - multiple traits may be inherited by the same class
   * 3 - traits are behavior
   */
}
