package general

import engine.core.{Entity, World}
import engine.input.MouseCode
import engine.math.{Vector2, pi}
import engine.render.renderer.render_element.{NGonRenderElement, PolygonRenderElement}
import lib.instance_manager.Ref
import org.dyn4j.dynamics.Body
import org.dyn4j.geometry.{Geometry, MassType}

class Dyn4j private(using world: World) extends Entity {

  val dyn4jWorld = org.dyn4j.world.World[Body]()
  dyn4jWorld.setGravity(0, -9.8 * 30.0)
  val floor = Body()
  floor.addFixture(Geometry.createRectangle(50.0, 0.6))
  floor.setMass(MassType.INFINITE)
  floor.translate(100.0, 30.0)
  floor.rotateAboutCenter(pi * 0.02)
  dyn4jWorld.addBody(floor)
  val body01 = Body()
  body01.addFixture(Geometry.createCircle(10.0))
  body01.translate(100.0, 100.0)
  body01.setMass(MassType.NORMAL)
  dyn4jWorld.addBody(body01)

  val dyn4jVisualScale = 5.0f

  val floorVisuals = Entity("Floor Visuals")
  floorVisuals.addRenderElement(
    PolygonRenderElement(Vector(
      Vector2(-25, -0.3),
      Vector2(-25, 0.3),
      Vector2(25, 0.3),
      Vector2(25, -0.3),
    ).map(_ * dyn4jVisualScale))
  )
  val body01Visuals = Entity("Body01 Visuals")
  body01Visuals.addRenderElement(
    NGonRenderElement(10.0f * dyn4jVisualScale)
  )

  //  world.onInit += (_ => ???)


  world.onUpdate += (deltaTime =>
    dyn4jWorld.update(deltaTime)

    // Update visuals positions
    val floorTranslation = floor.getTransform.getTranslation
    floorVisuals.globalPosition = Vector2(floorTranslation.x, floorTranslation.y) * dyn4jVisualScale
    floorVisuals.globalRotation = floor.getTransform.getRotationAngle.toFloat
    val body01Translation = body01.getTransform.getTranslation
    body01Visuals.globalPosition = Vector2(body01Translation.x, body01Translation.y) * dyn4jVisualScale
    body01Visuals.globalRotation = body01.getTransform.getRotationAngle.toFloat

    val colDataIterator = dyn4jWorld.getCollisionDataIterator
    while (colDataIterator.hasNext)
      val cd = colDataIterator.next()
      if (cd.isContactConstraintCollision) {
        val cc = cd.getContactConstraint

//        cc.getContacts.forEach(c =>
//          println(c.getPoint)
//        )
      }


    if (world.input.pressed(MouseCode.left))
      val body01Force = (world.input.mousePosition - body01Visuals.globalPosition) * 170.0f
      body01.applyForce(org.dyn4j.geometry.Vector2(body01Force.x.toDouble, body01Force.y.toDouble))
    )

  override def ref: Ref[Dyn4j, Entity] =
    super.ref.asInstanceOf[Ref[Dyn4j, Entity]]
}

object Dyn4j {
  def apply(name: String)(using world: World): Dyn4j = {
    val entity = new Dyn4j()
    entity.name = name
    entity.makeReady()
    entity
  }
}