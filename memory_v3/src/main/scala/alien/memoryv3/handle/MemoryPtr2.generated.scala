package alien.memoryv3.handle

import alien.memoryv3.*

import scala.Specializable.AllNumeric

case class MemoryPtr2[L <: Layout, @specialized(AllNumeric) T] private[alien] (
    private[alien] val offset0: Long,
    private[alien] val step1: Long,
    private[alien] val offset1: Long,
    private[alien] val step2: Long,
    private[alien] val offset2: Long,
)
