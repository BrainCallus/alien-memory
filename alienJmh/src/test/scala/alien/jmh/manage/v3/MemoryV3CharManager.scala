package alien.jmh.manage.v3

import alien.memoryv3.*

case class MemoryV3CharManager(n: Int) extends MemoryV3ManagerBase[Char](n) {

  override protected def getValues: Value[Char] = Values.Char

  override def provideGet(i: Int): Char = ptr.get(memory, i)

  override def provideSet(v: Char, i: Int): Unit = ptr.set(memory, v, i)
}
