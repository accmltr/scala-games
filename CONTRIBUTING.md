# Principles
## Exceptions
Exceptions should be thrown at value assignment, not when values are used.

# Mutable Front, Immutable Back

When working on games, every object that stores some game state should be mutable, since it makes it easier to work with and also is designed according to its intended purpose (mutable state). 

When creating engine systems, prioritize immutability! For the engine to have many systems working inside of it, all running in certain sequences in the pipeline, working with immutable objects is almost a neccessity. Allowing mutability makes it very hard to write safe systems that don't have insurmountable bugs that cannot be seen at compile time.

### As an example, let us take a look at how the `engine.render.renderer` system has been implemented

The  `Renderer` only accepts `RenderData` objects in its `render` method. The `RenderData` class is completely immutable, and only serves as a snapshot of the mutable `RenderElement` class used by the engine users.

Every frame the `RenderElement` objects that need to be rendered by the engine gets converted into the immutable snapshots of their state before they get passed on to the current `Renderer`.

## Mutable Interface Design Pattern

When making classes with mutable properties, it currently seems to be impossible to have the construction parameter names be the same as their setters and getters, which makes the interface ugly and causes issues when refactoring properties to have private values and public setters and getters, e.g. `character.speed = 7` becomes `character.setSpeed = 7` or `character.setSpeed(7)`.

### Let us look at a failing example

Say we want to create a built-in `Player` class in the engine, with a mutable `health: Float` property. You might start off with something like this:
```
case class Player(var health: Float)
```
Then you start creating a trial game to test the class, and end up with the following somewhere inside your game loop:
```
// --> ...inside some update callback of the ExplosionManager

def processExplosion(explosionHits: Set[Player]) =
  if (explosionHits.contains(player))
    player.health -= 7

// <-- End of snippet
```
Then you decide that `Player.health` should never be allowed to go below `0`, since having `Player` instances with negatvie `health` values might have unintended consequences in certain use-cases for the users of the engine. So you try to enforce that in the `Player` constructor and when the `health` property is set:
```
case class Player(private var _health: Float) {
  require(_health >= 0, "health must be >= 0")
  def health: Float = _health
  def health_=(value: Float): Unit =
    require(value >= 0, "health must be >= 0")
    _health = value
}
```

This works great, now an `IllegalArgumentException` is thrown whenever the user tries to pass a negative value for the `_health` parameter in the constructor, or tries to set `health` to a negative value via the `health_=` setter.

However, now we have a slight discrepency in the way our `Player` interface looks to the user. Users might see that the `Player` constructor's parameter is called `_health`, and the `Player` has a `health` property, which could confuse some users who has never come accross these types of design patterns before.
![image](https://github.com/accmltr/scala-games/assets/19354678/4b228b53-f548-4627-896b-ec748ecec86d)

Even for advanced users, this can be a great annoyance. Since some users tend to prefer writing out the parameter names of the methods/constructors they call explicitly, but then there is always an underscore in front of properties that implement this pattern:
```
val player = Player(_health = 10)
println(player.health)
```

This might seem like a minor inconvenience, but it is something that adds more overhead when coding, which goes against the goal of creating an engine that is easy to use. As we will see in the next section however, there is a clean solution.

### How to fix the problem: Companion Objects

Here is the code that provides a consistent interface for the `Player` class:
```
case class Player private () {
  private var _health: Float =
    0 // Health is private, but the companion object may still access it.
  def health: Float = _health
  def health_=(value: Float): Unit =
    require(value >= 0, "health must be >= 0")
    _health = value
}

object Player {
  def apply(health: Float): Player = {
    val player = new Player()
    player.health =
      health // DO NOT use `player._health = health`, this will bypass the `require` check.
    player
  }
}
```

Now the user code in our previous example:
```
val player = Player(_health = 10)
println(player.health)
```
Becomes:
```
val player = Player(health = 10)
println(player.health)
```

And now we have successfully created a mutable class, enforces argument requirements upon value assignment, instead of checking for exceptions down the line, and leaving the search for the source of the exception up to the user.

#### Tip

As noted in the comment inside the companion object, be careful to not set the private version of the property you are working with, as this bypasses the `require` checks on your properties. If you want to be a bit more cautious, you could make a mental note to always run your requirement checks manually inside your companion object constructors:

```
case class Player private () {
  private var _health: Float =
    0 // Health is private, but the companion object may still access it.
  def health: Float = _health
  def health_=(value: Float): Unit =
    _checkHealth()
    _health = value

  private def _checkHealth(): Unit =
    require(_health >= 0, "health must be >= 0")

  private def _runChecks(): Unit = {
    _checkHealth()
  }
}

object Player {
  def apply(health: Float): Player = {
    val player = new Player()
    player._health = health
    player._runChecks()
    player
  }
}
```
