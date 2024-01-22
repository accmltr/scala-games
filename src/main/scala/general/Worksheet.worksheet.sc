enum MyLayer(val value: Float):
  case Background extends MyLayer(0)
  case Foreground extends MyLayer(10)
  case UI extends MyLayer(100)
  case UI_Overlay extends MyLayer(100.1)

// Print the name and value of the 'Background' layer
val name: String = MyLayer.Background.toString
val value: Float = MyLayer.Background.value

println(s"Name: $name, Value: $value")
