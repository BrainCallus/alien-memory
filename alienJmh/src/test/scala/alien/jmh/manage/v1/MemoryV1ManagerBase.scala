package alien.jmh.manage.v1

import alien.jmh.manage.MemoryResourceManager
import alien.memory.*

abstract class MemoryV1ManagerBase[@specialized T](size: Int)
    extends MemoryResourceManager[T] {
  implicit val region = Region.fresh.newShared()

  val layout = getValues * size
  val memory = Memory.allocate(layout)
  val ptr    = layout / % / $

  protected def getValues: Value[T]
}
