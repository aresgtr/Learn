# JVM Thread Communication
## The Producer-Consumer Problem
producer → [ ? ] → consumer
```scala
class SimpleContainer {
  private var value: Int = 0

  def isEmpty: Boolean = value == 0
  def set(newValue: Int) = value = newValue
  def get = {
    val result = value
    value = 0
    result
  }
}

def naiveProdCons(): Unit = {
  val container = new SimpleContainer

  val consumer = new Thread(() => {
    println("[consumer] waiting...")
    while(container.isEmpty) {
      println("[consumer] actively waiting...")
    }

    println("[consumer] I have consumed " + container.get)
  })

  val producer = new Thread(() => {
    println("[producer] computing...")
    Thread.sleep(500)
    val value = 42
    println("[producer] I hace produced, after long work, the value " + value)
  })

  consumer.start()
  producer.start()
}

naiveProdCons()
```
⬆️ Note: this test assumes the consumer starts first and waits for the producer.

It may be the case that the producer may start and finish first, and the consumer is stuck waiting. This simple implementation does not include that case.
```text
...
...
[consumer] actively waiting...
[consumer] actively waiting...
[consumer] actively waiting...
[producer] I have produced, after long work, the value 42
[consumer] actively waiting...
[consumer] I have consumed 42
```
All the code above ⬆️ is very dumb. 🙈 All the busy waiting is computing waste. Let's improve this.

> __Synchronized__
> 
> Entering a synchronized expression on an object _locks_ the object:
> ```scala
> val someObject = "hello"
>
> someObject.synchronized {  // ← lock the object's monitor
>   // code                     ← any other thread trying to run this will block
> }                          // ← release the lock
> ```
> monitor: a data structure internally used by the JVM to keep track of which object is locked by which thread.
>
> Only __AnyRefs__ can have synchronized blocks. Primitive types (Int, Boolean) do not have synchronized expressions.
>
> General principles:
> - make no assumptions about who gets the lock first
> - keep locking to a minimum
> - maintain _thread safety_ at ALL times in parallel applications

> __*wait()* and *notify()*__
>
> wait() -ing on an object's monitor suspends you (the thread) indefinitely
> ```scala
> // thread 1
> val someObject = "hello"
> someObject.synchronized {  // ← lock the object's monitor
>   // ... code part 1
>   someObject.wait()        // ← release the lock and... wait
>   // ... code part 2          ← when allowed to proceed, lock the monitor again and continue
> }
> ```
> ```scala
> // thread 2
> someObject.synchronized {  // ← lock the object's monitor
>   // ... code
>   someObject.notify()      // ← signal ONE sleeping thread they may continue. Which one? You don't know!
>   // ... more code
> }                          // ← but only after I'm done and unlock the monitor
> ```
> Use _notifyAll()_ to awaken ALL threads
>
> Waiting and notifying only work in _synchronized_ expressions.

## wait and notify
```scala
def smartProdCons(): Unit = {
  val container = new SimpleContainer

  val consumer = new Thread (() => {
    println("[consumer] waiting...")
    container.synchronized {
      container.wait()
    }

    // at this point, container must have some value
    println("[consumer] I have consumed " + container.get)
  })

  val producer = new Thread(() => {
    println("[producer] Hard at work...")
    Thread.sleep(2000)
    val value = 42

    container.synchronized {
      println("[producer] I'm producing " + value)
      container.set(value)
      container.notify()
    }
  })

  consumer.start()
  producer.start()
}

smartProdCons()
```
⬇️ There is no busy waiting. Smart! ( ͡° ͜ʖ ͡°)
```text
[consumer] waiting...
[producer] Hard at work...
[producer] I'm producing 42
[consumer] I have consumed 42
```
