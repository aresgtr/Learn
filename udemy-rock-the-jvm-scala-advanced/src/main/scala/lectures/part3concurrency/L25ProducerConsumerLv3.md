# Producer-Consumer, Level 3 + Exercise

```text
producer1 → [ ? ? ? ] → consumer1
producer2 ↗          ↘ consumer2
```
```scala
class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
  override def run(): Unit = {
    val random = new Random()

    while(true) {
      buffer.synchronized {
        /*
          Producer produces value, 2 Cons are waiting.
          Notifies 1 consumer, notifies on buffer.
          Notifies the other consumer.
        */
        while (buffer.isEmpty) {
          println(s"[consumer $id] buffer empty, waiting...")
          buffer.wait()
        }

        // there must be at least 1 value in the buffer
        val x = buffer.dequeue() // OOps!
        println(s"[consumer $id] consumed + x")

        buffer.notify()
      }

      Thread.sleep(random.nextInt(500))
    }

  }
}

class Producer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
  override def run(): Unit = {
    val random = new Random()
    var i = 0

    while(true) {
      buffer.synchronized {
        while (buffer.size == capacity) {
          println(s"[producer $id] buffer is full, waiting...")
          buffer.wait()
        }

        // there must be at least 1 empty space in the buffer
        println(s"[producer $id] producing " + i)
        buffer.enqueue(i)

        buffer.notify()

        i += 1
      }

      Thread.sleep(random.nextInt(500))
    }

  }
}

def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
  val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
  val capacity = 3

  (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
  (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
}

multiProdCons(nConsumers=3, nProducers=3)
```
As a result:
```text
[consumer 1] buffer empty, waiting...
[producer 3] producing 0
[producer 2] producing 0
[producer 1] producing 0
[consumer 2] consumed 0
[consumer 3] consumed 0
[consumer 1] consumed 0
[producer 2] producing 1
[producer 2] producing 2
[consumer 3] consumed 1
[producer 3] producing 1
[consumer 1] consumed 2
...
```
