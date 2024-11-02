package alien.jmh.manage.v3

import alien.memoryv3.*

case class MemoryV3DoubleManager(n: Int)
    extends MemoryV3ManagerBase[Double](n) {

  override protected def getValues: Value[Double] = Values.Double

  override def provideGet(i: Int): Double = ptr.get(memory, i)

  override def provideSet(v: Double, i: Int): Unit = ptr.set(memory, v, i)
}
