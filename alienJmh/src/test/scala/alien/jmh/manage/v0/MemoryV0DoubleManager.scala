package alien.jmh.manage.v0

import alien.memoryv0.*

case class MemoryV0DoubleManager(n: Int)
    extends MemoryV0ManagerBase[Double](n) {

  override protected def getValues: Value[Double] = Values.Double

  override def provideGet(i: Int): Double = ptr.get(memory, i)

  override def provideSet(v: Double, i: Int): Unit = ptr.set(memory, v, i)
}
