package lectures.part5ts

object L44Variance extends App {

  // Jungle Hierarchy
  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  /**
   * What is variance?
   * "inheritance" - type substitution of generics
   */

  // for example
  class Cage[T]
  // ❓ Since Cat extends Animal, should a Cage[Cat] inherit from Cage[Animal]?

  // 👁‍🗨 Possible answer: Yes - covariance ⬇️
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat] //✔️

  // 👁‍🗨 Possible answer: No - invariance
  class ICage[T]
//  val icage: ICage[Animal] = new ICage[Cat] //✖️

  // 👁‍🗨 Possible answer: Hell No - contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]  //✔


  class InvariantCage[T](animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T)  // COVARIANT POSITION

//  class ControvariantCage[-T](val animal: T)
  /*
    ⬆️ is not correct, because the compiler sees it as
      val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

//  class CovariantVariableCage[+T](var animal: T)
  /*
    ⬆️ is not correct, because the compiler sees it as
      val ccage: CCage[Animal] = new CCage[Cat](new Cat)
      ccage.animal = new Crocodile
   */

  class InvariantVariableCage[T](var animal: T) // ok


//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T)  // CONTRAVARIANT POSITION
//  }
  /*
    ⬆️ not work because:
      val ccage: CCage[Animal] = new CCage[Dog]
      ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true   // ✔️
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)


  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]  // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION.

  // Return Types
  class PetShop[-T] {
//    def get(isItaPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShip[Animal] {
        def get(isItaPuppy: Boolean): Animal = new Cat
      }

      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true) // EVIL CAT!
     */

    // solution
    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
//  val evilCat = shop.get(true, new Cat) // illegal

  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)  // ✔️

  /**
   * Big Rule
   * - method arguments are in CONTRAVARIANT position
   * - return types are in COVARIANT position
   */

  /*
    Exercise - design a parking API
    1. invariant, covariant, contravariant

      Parking[T](things: List[T]) {
        park(vehicle: T)
        impound(vehicles: List[T])
        checkVehicles(conditions: String): List[T]
      }
   */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  // invariant
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???   // remove these vehicles from parking
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  // covariant
  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ??? // widening our type
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions:String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  // contravariant
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ??? // acceptable
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???
    /*
      def flatMap[R <: T, S](f: Function1[T, XParking[S]]): XParking[S] = ???
        Function1 is contravariant in T
        - inverse variance relationship with T
        - a double contravariant position => covariant
     */
  }

  /**
   * Rule of thumb
   * - use covariance = COLLECTION OF THINGS
   * - use contravariance = GROUP OF ACTIONS
   */

  /*
    2. use someone else's API: IList[T]
   */
  class IList[T]

  // covariant
  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ??? // widening our type
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions:String): IList[S] = ???
  }

  // contravariant
  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ??? // type restriction
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }
  /*
    3. Parking = monad!
        - flatMap see above 👆
   */
}
