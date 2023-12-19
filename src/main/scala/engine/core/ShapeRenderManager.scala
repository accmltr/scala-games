package engine.core

/** Optimizes the rendering of shapes for a new frame.
  *
  * Groups all shapes by layer and then by shader ID.
  *
  * After grouping it creates a vertex buffer for each leaf group and renders
  * the group in one draw call.
  *
  * Note: This is managed by the super render manager and given calls each time
  * a layer is to be rendered.
  */
object ShapeRenderManager {}
