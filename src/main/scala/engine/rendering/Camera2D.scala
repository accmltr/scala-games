package engine.rendering

import org.joml._
import engine.math.Vector2
import engine.math.Vector2Implicits.given
import engine.rendering.window.ScreenSize
import engine.rendering.window.ScreenSizeImplicits.given
import engine.rendering.window.ScreenSizeImplicits

class Camera2D(
    var position: Vector2 = Vector2(0, 0),
    projectionSize: Vector2 = ScreenSize.p720
) {
  private var _projMatrix, _viewMatrix, _inverseMatrix: Matrix4f =
    new Matrix4f()

  projectionMatrix = (projectionSize)

  def projectionMatrix_=(size: Vector2 = projectionSize): Unit =
    _projMatrix.setOrtho2D(0, size.x, size.y, 0)
  def projectionMatrix: Matrix4f = _projMatrix

  def viewMatrix: Matrix4f =
    val cameraFront: Vector3f = new Vector3f(0, 0, -1)
    val cameraUp: Vector3f = new Vector3f(0, 1, 0)
    _viewMatrix.identity()
    _viewMatrix.lookAt(
      new Vector3f(position.x, position.y, 1),
      cameraFront.add(position.x, position.y, 0),
      cameraUp
    )
    _inverseMatrix = Matrix4f(_viewMatrix).invert()
    _viewMatrix

}
