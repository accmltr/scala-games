import lib.procedure_tracker.{Procedure, ProcedureController}

val animationOpController = ProcedureController()
val animationOp = animationOpController.op


animationOp.onPaused += (_ => println("Paused my animation!"))

animationOpController.start()
animationOpController.pause()

//import lib.property_tracker.PropertyController
//
//val health = PropertyController(100f)
//
//
//health.value
//
//
//def f(t: (Float, Float)): Unit = println(t)
//health.property.onChange += f
//
//
//health.value += 54
//
//health.value
//
//
