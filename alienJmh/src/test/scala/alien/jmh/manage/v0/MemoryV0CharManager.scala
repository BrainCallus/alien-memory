package alien.jmh.manage.v0

import alien.memoryv0.*

case class MemoryV0CharManager(n: Int) extends MemoryV0ManagerBase[Char](n) {

  override protected def getValues: Value[Char] = Values.Char

  override def provideGet(i: Int): Char = ptr.get(memory, i)

  override def provideSet(v: Char, i: Int): Unit = ptr.set(memory, v, i)
}
