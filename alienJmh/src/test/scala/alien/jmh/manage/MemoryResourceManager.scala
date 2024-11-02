package alien.jmh.manage

abstract class MemoryResourceManager[@specialized T] {
  def provideGet(i: Int): T
  def provideSet(v: T, i: Int): Unit
}
