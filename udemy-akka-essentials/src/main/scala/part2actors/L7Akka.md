# The Akka Actor Model to the Rescue

Actors inspired the object oriented paradigm back in the 1970s, but somehow the computer programming history took a
different turn and created the classes and objects we know today.

With __traditional__ objects:
- we store their state as data
- we call their methods

With __Actors__:
- We store their state as data
- We send messages to them, asynchronously

Actors are objects we can't access directly, but only send messages to.

> ## A Simple Example
>
> I'd like to know when Bon Jovi 🎸 will play next in London. 🇬🇧
> 
> My friend Alex knows.
> 
> ### Normal exchange:
> 
> Daniel: "Hey Alex, when is BJ playing next in London?"
> 
> (after 2 seconds)
> 
> Alex: "On November 20."
> 
> ### Wierd exchange:
> 
> (wait until I get a hold of her brain)
> 
> alex.getUpcomingConvertDates().get("Bon Jovi")

## Some Natural Principles

1. Every interaction happens via sending and receiving messages.

2. Messages are asynchronous by nature.
   - it takes time for a message to travel.
   - sending and receiving may not happen at the same time...
   - ...or even in the same context.

3. I can't poke into my friend's brain! 👉👩