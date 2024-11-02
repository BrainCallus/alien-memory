package alien.jmh.manage.v1

import alien.memory.*

case class MemoryV1DoubleManager(n: Int)
    extends MemoryV1ManagerBase[Double](n) {

  override protected def getValues: Value[Double] = Values.Double

  override def provideGet(i: Int): Double = ptr.get(memory, i)

  override def provideSet(v: Double, i: Int): Unit = ptr.set(memory, v, i)
}
