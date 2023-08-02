package lectures.part2afp

object L19Monads extends App {

  // Our own Try Monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] = // Call-by-Name: We don't want the argument to be evaluated when we build Attemp
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this // Fail flatMap anything will just return the failure
  }

  /*
    left-identity

    unit.flatMap(f) = f(x)
    Attempt(x).flatMap(f) = f(x) // Success case!
    Success(x).flatMap(f) = f(x) // proved.

    right-identity
    attempt.flatMap(unit) = attempt
    Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
    Fail(e).flatMap(...) = Fail(e)

    associativity

    attemp.flatMap(f).flatMap(g) == attemp.flatMap(x => f(x).flatMap(g))
    Fail(e).flatMap(f).flatMap(g) = Fail(e)
    Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

    Success(v).flatMap(f).flatMap(g) =
      f(v).flatMap(g) OR Fail(e)

    Success(v).flatMap(x => f(x).flatMap(g)) =
      f(v).flatMap(g) OR Fail(e)
   */

  // See if it is working
  val attempt = Attempt{
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)  //>> Fail(java.lang.RuntimeException: My own monad, yes!)


  /*
    EXERCISE:
    1) implement a Lazy[T] monad = computation which will only be executed when it's needed.
      unit/apply
      flatMap
   */

  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value

    def use: A = internalValue  // just for test
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything") // should not print to console if not evaluated
    42
  }

  println(lazyInstance.use) // print both lines because now we evaluate it

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })  // should not print

  flatMappedInstance.use

  /*
    left-identity
    unit.flatMap(f) = f(v)
    Lazy(v).flatMap(f) = f(v)

    right-identity
    l.flatMap(unit) = l
    Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

    associativity: l.flatMap(f).flatMap(g) = l.flatMap(x => f(x).flatMap(g))

    Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
    Lazy(v).flatMap(x => f(x).flatMap(g)) = f(x).flatMap(g)
   */

  /*
    EXERCISE
      2) Monads = unit + flatMap
       Monads = unit + map + flatten  (alternative way to define a monad)

       How to transform the first (unit + flatMap) to the second (unit + map + flatten)?

       Monad[T] {

        def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)

        def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
        def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)

        List(1, 2, 3).map(_ * 2) = List(1, 2, 3).flatMap(x => List(x * 2))
        List(List(1, 2), List(3, 4)).flatten = List(List(1, 2), List(3, 4)).flatMap(x => x) = List(1, 2, 3, 4)
      }

      (have List in mind)
   */
}
