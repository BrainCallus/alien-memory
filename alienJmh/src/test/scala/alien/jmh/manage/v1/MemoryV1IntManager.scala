package alien.jmh.manage.v1

import alien.memory.*

case class MemoryV1IntManager(n: Int) extends MemoryV1ManagerBase[Int](n) {

  override def provideGet(i: Int): Int = {
    ptr.get(memory, i)
  }

  override def provideSet(v: Int, i: Int): Unit = ptr.set(memory, v, i)
  override protected def getValues: Value[Int]  = Values.Int

}
