package alien.jmh.manage.v1

import alien.memory.*

case class MemoryV1CharManager(n: Int) extends MemoryV1ManagerBase[Char](n) {

  override protected def getValues: Value[Char] = Values.Char

  override def provideGet(i: Int): Char = ptr.get(memory, i)

  override def provideSet(v: Char, i: Int): Unit = ptr.set(memory, v, i)
}
