package engine.data_structure

trait MutableTree[T] extends Iterable[T & MutableTree[T]] {

  this: T =>

  override final def iterator: Iterator[T & MutableTree[T]] = {
    Iterator(this) ++ children.iterator.flatMap(_.iterator)
  }

  var parent: Option[T & MutableTree[T]]
  var children: List[T & MutableTree[T]]
}
