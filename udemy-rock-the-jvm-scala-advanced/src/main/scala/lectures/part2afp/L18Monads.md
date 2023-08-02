# Monads
## Intro
Think of a list of elements.

Monads are a linkd of types which have some fundamental operations.
```scala
// 2 fundamental operations of a monad
trait MonadTemplate[A] {
  def unit(value: A): MonadTemplate[A]  // also called apply (in scala) or pure (in other languages)
  def flatMap[B](f: A => MonadTemplate[B]): MonadTemplate[B]  // also called bind (in other languages)
}
```
List, Option, Try, Future, Stream, Set are all _monads_.

Operations must satisfy the _monad laws_:
- left identity `unit(x).flatMap(f) == f(x)`
- right identity `aMonadInstance.flatMap(unit) == aMonadInstance`
- associativity `m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))`

## Example: List
Left Identity:
```scala
List(x).flatMap(f) =
f(x) ++ Nil.flatMap(f) =
f(x)
```

Right Identity:
```scala
list.flatMap(x => List(x)) =
list
```

Associativity:
```scala
[a b c].flatMap(f).flatMap(g) = 
(f(a) ++ f(b) ++ f(c)).flatMap(g) =
f(a).flatMap(g) ++ f(b).flatMap(g) ++ f(c).flatMap(g) =
[a b c].flatMap(f(_).flatMap(g)) =
[a b c].flatMap(x => f(x).flatMap(g))
```

## Another Example: Option
Left identity:
```scala
Option(x).flatMap(f) = f(x)
Some(x).flatMap(f) = f(x)
```

Right identity:
```scala
opt.flatMap(x => Option(x)) = opt
```
```scala
Some(v).flatMap(x => Option(x)) =
Option(v) =
Some(v)
```

Associativity:
```scala
o.flatMap(f).flatMap(g) = o.flatMap(x => f(x).flatMap(g))
```
```scala
Some(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
Some(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
```