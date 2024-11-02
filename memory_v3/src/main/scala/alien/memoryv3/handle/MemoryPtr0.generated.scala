package alien.memoryv3.handle

import alien.memoryv3.*

import scala.Specializable.AllNumeric

case class MemoryPtr0[L <: Layout, @specialized(AllNumeric) T] private[alien] (
    private[alien] val offset0: Long,
)
