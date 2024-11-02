package alien.jmh.manage.v3

import alien.memoryv3.*

case class MemoryV3IntManager(n: Int) extends MemoryV3ManagerBase[Int](n) {

  override def provideGet(i: Int): Int = {
    ptr.get(memory, i)
  }

  override def provideSet(v: Int, i: Int): Unit = ptr.set(memory, v, i)
  override protected def getValues: Value[Int]  = Values.Int

}
