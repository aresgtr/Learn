package lectures.part5ts

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object L50HigerKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  // use case
  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
  }

  // ⬆️ are almost the same

  // Say we combine/multiply List(1, 2) x List("a", "b") => List(1a, 1b, 2a, 2b)
  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiply[A, B](listA: Option[A], listB: Option[B]): Option[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiply[A, B](listA: Future[A], listB: Future[B]): Future[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  // ⬆️ are almost the same

  // Because we are pro, we want common implementation
  // Because List, Option, and Future are completely unrelated, we need Higher Kinded Types
  {
    trait Monad[F[_], A] {
      def flatMap[B](f: A => F[B]): F[B]

      def map[B](f: A => B): F[B]
    }

    class MonadList[A](list: List[A]) extends Monad[List, A] {
      override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)

      override def map[B](f: A => B): List[B] = list.map(f)
    }

    class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
      override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)

      override def map[B](f: A => B): Option[B] = option.map(f)
    }

    def multiply[F[_], A, B](ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
      for {
        a <- ma
        b <- mb
      } yield (a, b)

    val monadList = new MonadList(List(1, 2, 3))
    monadList.flatMap(x => List(x, x + 1)) // List[Int]
    // Monad[List, Int] => List[Int]

    monadList.map(_ * 2) // List[Int]
    // Monad[List, Int] => List[Int]

    println(multiply(new MonadList(List(1, 2)), new MonadList(List("a", "b")))) //>> List((1,a), (1,b), (2,a), (2,b))
    println(multiply(new MonadOption[Int](Some(2)), new MonadOption[String](Some("scala")))) //>> Some((2,scala))
  }


  // even more improvement
  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]

    def map[B](f: A => B): F[B]
  }

  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)

    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)

    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  def multiply[F[_], A, B](implicit ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  val monadList = new MonadList(List(1, 2, 3))
  monadList.flatMap(x => List(x, x + 1)) // List[Int]
  // Monad[List, Int] => List[Int]

  monadList.map(_ * 2) // List[Int]
  // Monad[List, Int] => List[Int]

  println(multiply(List(1, 2), List("a", "b"))) //>> List((1,a), (1,b), (2,a), (2,b))
  println(multiply(Some(2), Some("scala"))) //>> Some((2,scala))
}
