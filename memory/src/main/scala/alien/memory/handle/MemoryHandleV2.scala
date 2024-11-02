//package alien.memory.handle
//
//import alien.memory.*
//import alien.memory.layout.Layout
//import jdk.internal.vm.annotation.IntrinsicCandidate
//
//import java.lang.foreign.MemoryLayout.PathElement
//import java.lang.foreign.{MemoryLayout, ValueLayout}
//import java.lang.invoke.VarHandle
//
//object MemoryHandleV2 {
//  val intVh = MemoryLayout
//    .sequenceLayout(
//      Long.MaxValue / ValueLayout.JAVA_INT.byteSize(),
//      ValueLayout.JAVA_INT,
//    ).varHandle(PathElement.sequenceElement())
//
//  val boolVh = MemoryLayout
//    .sequenceLayout(
//      Long.MaxValue / ValueLayout.JAVA_BOOLEAN.byteSize(),
//      ValueLayout.JAVA_BOOLEAN,
//    ).varHandle(PathElement.sequenceElement())
//  trait Base1Ops[L <: Layout, @specialized T]  {
//    protected val vh: MemoryPtr1[L, T]
//    @IntrinsicCandidate
//    @inline
//    protected def varHandle: VarHandle
//    @IntrinsicCandidate
//    @inline
//    def getF[R<:Global](mem: Memory[L, R], x1: Int)(implicit region: Region[R]): T  =
//      varHandle.get(mem.asJava, vh.offset0 + vh.step1 * x1 + vh.offset1, 0L).asInstanceOf[T]
//
//    def setF[R <: Global](mem: Memory[L, R], value: T, x1: Int)(
//      implicit
//      region: Region[R],
//    ): Unit = varHandle.set(mem.asJava, vh.offset0 + vh.step1 * x1 + vh.offset1, 0L, value.asInstanceOf[AnyRef])
//  }
//
//
//  implicit class Int1Ops[L<:Layout](
//                                     protected val vh: MemoryPtr1[L, Int],
//                                   ) extends Base1Ops[L, Int]  {
//
//    override protected def varHandle: VarHandle = intVh
//  }
//
//  implicit class Boolean1Ops[L<:Layout](
//                                     protected val vh: MemoryPtr1[L, Boolean],
//                                   ) extends Base1Ops[L, Boolean]  {
//
//    override protected def varHandle: VarHandle = boolVh
//  }
//
//  def main(args:Array[String]) = {
//    implicit val region = Region.fresh.newShared()
//
//    val layout = Values.Int * 10
//    val memory = Memory.allocate(layout)
//    val ptr = layout / % / $
//    val value = ptr.getF(memory, 0)
//    val toSet = value * 9 + 3
//    intVh.set(memory.asJava,ptr.offset0 + ptr.step1 * 1 + ptr.offset1, 0L, value )
//    ptr.setF(memory, toSet, 1)
//    println(ptr.getF(memory, 1))
//  }
//
//}
