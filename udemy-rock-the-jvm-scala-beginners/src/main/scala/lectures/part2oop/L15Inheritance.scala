package lectures.part2oop

object L15Inheritance extends App {

  // Single class inheritance - you can only extend one class at a time
  class Animal {
    val creatureType = "wild"
    def eat = println("nomnom")
    private def eat2 = println("nomnom")    // private - only accessible within this class
    protected def eat3 = println("nomnom")  // protected - only accessible within this class & subclasses
  }

  class Cat extends Animal {
    def crunch = {
      eat3  // super.eat3
      println("crunch crunch")
    }
  }

  val cat = new Cat
  cat.eat   //>> nomnnom
//  cat.eat2  // not accessible

//  cat.eat3  // not accessible outside the subclass
  cat.crunch  //>> nomnom \n crunch crunch

  // Constructors
  class Person(name: String, age: Int)
  class Adult(name: String, age: Int, idCard: String) extends Person(name, age)

  // Overriding
  class Dog extends Animal{
    override val creatureType = "domestic"
    override def eat3 = println("crunch, crunch")
  }
  val dog = new Dog
  dog.eat   //>> crunch, crunch
  dog.creatureType  //>> domestic

  // you can override directly from constructor
  class Dog2(override val creatureType: String) extends Animal

  // Type substitution (broad: polymorphism)
  val unkownAnimal: Animal = new Dog()
  unkownAnimal.eat  //>> crunch, crunch

  // overRIDING - supplying a different implementation in derived classes
  // overLOADING - supplying multiple methods with different signatures but with the same name in the same class

  // Preventing overrides
  // 1 - final keyword on val/def
  // 2 - final keyword on class
  // 3 - seal the class (keyword sealed) = extend classes in THIS FILE, prevent extension in other files
}
