## DISCLAIMER
***This project and its design is in early stages. I am currently working on finishing a stable rendering pipeline, which should be the first major component of the engine that will be complete.***

# scala-games
A minimalistic game engine for rapid prototyping using Scala 3.

## Why Scala?

In short, power and clean code.

Here are some further points that cover the viability of using Scala to develop games:
 - **Write a solution once, use it everywhere (no duplicate patterns):** Scala allows you to create abstract solutions for the common patterns you need to solve in your projects. Which also makes it an outstanding choice for community made libraries.
 - **Concise and powerful code:** Where you boilerplate in other languages, you usually have a tidy solution in Scala, e.g. [Setters And Getters](https://github.com/accmltr/scala-games/wiki/Scala-Pros-And-Cons#Setters-And-Getters).

Checkout the [Scala Pros And Cons](https://github.com/accmltr/scala-games/wiki/Scala-Pros-And-Cons) wiki page for some examples of why Scala is a great language for game developers.

## Project Goals
 - Easy to learn for newcomers with a concise API.
 - Powerful features for prototyping game mechanics quickly.
 - Extensible and modular, so that developers may customize the engine to their liking by easily rewriting its components for their use cases or adding libraries to add content and case-specific features. (Abstractions where possible improve flexibility)
 - Become smaller and more refined over time, rather than larger and more monolithic. Extreneous features should be made available through community-made libraries. (This is great for maintainability and keeping a small learning curve)
 - Stable, flexible API.
 - Include generic libraries to reduce project startup time in the 'lib' package, e.g. a highly configurable levelling system.
 - Prioritize easy of use, over performance.

## To Avoid
 - Forcing a certain type of development style. Developers should be the ones to choose what they focus on and how they like to structure their projects. E.g. if you want to create a combat system, you shouldn't have to think about how things are rendered.
 - Unnecesary features, e.g. 3 different types of buttons. These types of features should be made available through seperate libraries by the community, so that the main project can stay focused on its goals and remain maintainable by hopefully becoming smaller, rather than larger over time. *The generic libraries mentioned in the Porject Goals are considered to be neccesary tools for rapid prototyping of game mechanics.*

## Roadmap
- Stable render-pipeline with basic features:
   - Basic classes for rendering: polygons, lines, sprites. (With batch-rendering for polygons and lines)
   - Compatibility with user defined `RenderManager`s and `RenderedElement`s for extensibility. (Basically making it possible for users to recreate the rendering pipeline or simply add custom particle systems, etc.)
   - Simple post-processing with bloom, anti-aliasing and the option for user defined shaders to be set instead of the built-in shaders. (The engine should come with multiple post-processing shaders to choose from, with one set by default ofcourse. E.g. one with just bloom, or one with anti-aliasing and bloom).
- Physics with:
   - Rigidbodies.
   - Non-rigidbodies for detection only.
- Compile time generated Node classes, which are memory safe and destroyable by the engine.
