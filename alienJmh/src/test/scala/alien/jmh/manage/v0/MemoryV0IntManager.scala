package alien.jmh.manage.v0

import alien.memoryv0.*

case class MemoryV0IntManager(n: Int) extends MemoryV0ManagerBase[Int](n) {

  override def provideGet(i: Int): Int = {
    ptr.get(memory, i)
  }

  override def provideSet(v: Int, i: Int): Unit = ptr.set(memory, v, i)
  override protected def getValues: Value[Int]  = Values.Int

}
