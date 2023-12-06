# JVM Thread Communication Exercise (notifyAll, Deadlock, and Livelock)

## 1) An example where notifyAll acts in a different way than notify

```scala
def testNotifyAll(): Unit = {
  val bell = new Object

  (1 to 5).foreach(i => new Thread(() => {
    bell.synchronized {
      println(s"[thread $i] waiting...")
      bell.wait()
      println(s"[thread $i] horray!")
    }
  }).start())

  new Thread(() => {
    Thread.sleep(2000)
    println("[announcer] Rock'n roll!")
    bell.synchronized {
      bell.notifyAll()
    }
  }).start()
}

testNotifyAll()
```
Which prints:
```text
[thread 1] waiting...
[thread 4] waiting...
[thread 5] waiting...
[thread 3] waiting...
[thread 2] waiting...
[announcer] Rock'n roll!
[thread 2] hooray!
[thread 3] hooray!
[thread 5] hooray!
[thread 4] hooray!
[thread 1] hooray!
```
If we use notify() instead of notifyAll(), only 1 thread wakes up. The other 4 threads are still blocked and waiting for notify, and the program stucks on running.

## 2) Deadlock

```scala
case class Friend(name: String) {
  def bow(other: Friend): Unit = {
    this.synchronized {
      println(s"$this: I am bowing to my friend $other")
      other.rise(other=this)
      println(s"$this: my friend $other has risen")
    }
  }

  def rise(other: Friend): Unit = {
    this.synchronized {
      println(s"$this: I am rising to my friend $other")
    }
  }

}

val sam = Friend("Sam")
val pierre = Friend("Pierre")

new Thread(() => sam.bow(pierre)).start()  // sam's lock, then pierre's lock
new Thread(() => pierre.bow(sam)).start()  // pierre's lock, then sam's lock
```
As a result:
```text
Friend(Sam): I am bowing to my friend Friend(Pierre)
Friend(Pierre): I am bowing to my friend Friend(Sam)
```
This is a classical example of a deadlock where 2 threads lock 2 objects in reverse order.

## 3) Livelock

```scala
case class Friend(name: String) {
  var side = "right"

  def switchSide(): Unit = {
    if (side == "right") side = "left"
    else side = "right"
  }

  def pass(other: Friend): Unit = {
    while (this.side == other.side) {
      println(s"$this: Oh, but please, $other, feel free to pass...")
      switchSide()
      Thread.sleep(1000)
    }
  }

}

val sam = Friend("Sam")
val pierre = Friend("Pierre")

new Thread(() => sam.pass(pierre)).start()
new Thread(() => pierre.pass(sam)).start()
```
As a result:
```text
Friend(Pierre): Oh, but please, Friend(Sam), feel free to pass...
Friend(Sam): Oh, but please, Friend(Pierre), feel free to pass...
Friend(Sam): Oh, but please, Friend(Pierre), feel free to pass...
Friend(Pierre): Oh, but please, Friend(Sam), feel free to pass...
Friend(Sam): Oh, but please, Friend(Pierre), feel free to pass...
Friend(Pierre): Oh, but please, Friend(Sam), feel free to pass...
...
```
No threads are blocked, but no threads are free to continue running, because they are yielding execution to each other.
