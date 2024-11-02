package alien.jmh.manage.v3

import alien.jmh.manage.MemoryResourceManager
import alien.memoryv3.*

abstract class MemoryV3ManagerBase[@specialized T](size: Int)
    extends MemoryResourceManager[T] {
  implicit val region = Region.fresh.newShared()

  val layout = getValues * size
  val memory = Memory.allocate(layout)
  val ptr    = layout / % / $

  protected def getValues: Value[T]
}
