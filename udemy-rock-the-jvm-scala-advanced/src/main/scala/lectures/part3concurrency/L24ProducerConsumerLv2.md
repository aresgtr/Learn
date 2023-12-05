# Producer-Consumer, Level 2

```scala
/*
  producer → [ ? ? ? ] → consumer
*/

def prodConsLargeBuffer(): Unit = {
  val buffer = mutable.Queue[Int] = new mutable.Queue[Int]
  val capacity = 3

  val consumer = new Thread(() => {
    val random = new Random()

    while(true) {
      buffer.synchronized {
        if (buffer.isEmpty) {
          println("[consumer] buffer empty, waiting...")
          buffer.wait()
        }

        // there must be at least ONE value in the buffer
        val x = buffer.dequeue()
        println("[consumer] consumed " + x)

        // Hey producer, there's empty space available, are you lazy?
        buffer.notify()  // The consumer has finished extracting the value. Just in case the producer is sleeping, send a signal.
      }

      Thread.sleep(random.nextInt(500))
    }
  })

  val producer = new Thread(() => {
    val random = new Random()
    var i = 0

    while(true) {
      buffer.synchronized {
        if (buffer.size == capacity) {
          println("[producer] buffer is full, waiting...")
          buffer.wait()
        }

        // there must be at least ONE EMPTY SPACE in the buffer
        println("[producer] producing " + i)

        // Hey consumer, new food for you!
        buffer.notify()  // Just in case the consumer is sleeping, send a signal. (Regardless of whether the consumer is sleeping or active.)

        i += 1
      }

      Thread.sleep(random.nextInt(500))
    } 
  })

  consumer.start()
  producer.start()
}

prodConsLargeBuffer()
```
This will make it run indefinitely.
```text
[producer] producing 0
[consumer] consumed 0
[consumer] buffer empty, waiting...
[producer] producing 1
[consumer] consumed 1
[producer] producing 2
[consumer] consumed 2
[producer] producing 3
[producer] producing 4
[consumer] consumed 3
[consumer] consumed 4
...
```
