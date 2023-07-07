# Moar Functional Collections!
## Functional Seq
_Seq_ are "callable" through an integer index:
```scala
trait Seq[+A] extends PartialFunction[Int, A] {
  def apply(index: Int): A  // ⬅️ "give me the element at 'index' in the sequence"
}
```
```scala
val numbers = List(1, 2, 3)
numbers(1) // 2
numbers(3) // java.lang.IndexOutOfBoundsException
```
_Seq_ are partially defined on the domain [0, ..., length -1]

_Seq_ are Partial Functions!

## Functional Map
_Map_ are "callable" through their keys:
```scala
trait Map[A, +B] extends PartialFunction[A, B] {
  def apply(key: A): B  // ⬅️ "give me the 'value' associated to 'key'"
  def get(key: A): Option[B]
}
```
```scala
val phoneMappings = Map(2 -> "ABC", 3 -> "DEF")
phoneMappings(2) // "ABC"
phoneMappings(1) // java.lang.NoSuchElementException
```
_Map_ is defined on the domain of its keys. (which is a subdomain on the type A)

_Map_ are Partial Functions!