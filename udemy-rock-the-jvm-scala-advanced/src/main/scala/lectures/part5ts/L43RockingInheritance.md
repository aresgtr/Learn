# Advanced Inheritance

## 1 - Convenience
### Example: small IO API for reading and writing

```scala
trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    // has access to all APIs
    stream.foreach(println)
    stream.close(0)
  }
```

## 2 - Diamond Problem
### Example: jungle hierarchy

```scala
trait Animal {
    def name: String
  }

  trait Lion extends Animal { override def name: String = "lion" }
  trait Tiger extends Animal { override def name: String = "tiger" }

  trait Mutant extends Lion with Tiger    // this is compilable

  class MutantC extends Lion with Tiger   // why it is not complaint? what's the name of this mutant? let's find out.

  val m = new MutantC
  println(m.name) //>> tiger
```

Because the compiler see this as:
```scala
MutantC extends Animal with { override def name: String = "lion" }
           with Animal with { override def name: String = "tiger" }
```

__*LAST OVERRIDE GETS PICKED!*__

## 3 - The Super Problem + Type Linearization
### Example: warm & code colors

```scala
trait Cold {
    def print = println("cold")
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  color.print
  //>> white
  //>> blue
  //>> green
  //>> cold

  // Why there is no red?
```
<img height="300" src="./img/IMG_1877.HEIC" width="600"/>

<img height="300" src="./img/IMG_1878.HEIC" width="600"/>