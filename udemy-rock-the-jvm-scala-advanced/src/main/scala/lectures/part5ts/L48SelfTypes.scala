package lectures.part5ts

object L48SelfTypes extends App {

  // 💡 Self Types are a way of requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  // SELF TYPE: This marker forces whoever implements Singer to implement Instrumentalist
  //  - you can name "self" whatever you want: A, scala, or even "this"
  trait Singer { self: Instrumentalist => // Not a lambda

    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???   // ✅
  }

  /*
  class Vocalist extends Singer {
    override def sing(): Unit = ???
  }

    ❎ This is illegal. It does not conform to Instrumentalist.
   */

  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???   // ✅
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???   // ✅
  }

  // 💡 vs inheritance
  class A
  class B extends A   // B is an A.

  trait T
  trait S { self: T => }  // S requires a T.

  // 💡 Self Types are used commonly in Cake Pattern ("dependency injection")
  //    - with a self type, we enforce the dependency on a type, which will be "injected" later.

  // Classical Dependency Injection
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)  // injected at runtime

  // CAKE PATTERN (difference from classical: check at compile time)
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics


  // 💡 Cyclical Dependencies
//  class X extends Y
//  class Y extends X   // ❎ not compile

  trait X { self: Y => }
  trait Y { self: X => }  // ✅
  // 👆 whoever implements X must implement Y, and whoever implements Y must implement X. 👍
}
