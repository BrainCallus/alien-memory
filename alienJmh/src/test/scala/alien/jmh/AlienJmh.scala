package alien.jmh

import alien.jmh.Generator.{GenMaterialize, ValuesRunPack, generateRunPack}
import alien.jmh.manage.v0.{
  MemoryV0CharManager,
  MemoryV0DoubleManager,
  MemoryV0IntManager,
}
import alien.jmh.manage.v1.{
  MemoryV1CharManager,
  MemoryV1DoubleManager,
  MemoryV1IntManager,
}
import alien.jmh.manage.v3.{
  MemoryV3CharManager,
  MemoryV3DoubleManager,
  MemoryV3IntManager,
}
import alien.jmh.manage.MemoryResourceManager
import org.openjdk.jmh.annotations.*
import zio.test.Gen

import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Timeout(time = 3, timeUnit = TimeUnit.MINUTES)
@Warmup(iterations = 20, time = 60)
@Measurement(iterations = 3, time = 60)
@Fork(value = 1)
@Threads(value = 1)
class AlienJmh {

  val size = 100_000

  var v0IntManager: MemoryV0IntManager       = _
  var v0DoubleManager: MemoryV0DoubleManager = _
  var v0CharManager: MemoryV0CharManager     = _
  var v1IntManager: MemoryV1IntManager       = _
  var v1DoubleManager: MemoryV1DoubleManager = _
  var v1CharManager: MemoryV1CharManager     = _
  var v3IntManager: MemoryV3IntManager       = _
  var v3DoubleManager: MemoryV3DoubleManager = _
  var v3CharManager: MemoryV3CharManager     = _

  var intPack: ValuesRunPack[Int]       = _
  var doublePack: ValuesRunPack[Double] = _
  var charPack: ValuesRunPack[Char]     = _

// Чтобы инициализация не влияла на тайминги
  @Setup
  def init(): Unit = {
    v0IntManager = MemoryV0IntManager(size)
    v0DoubleManager = MemoryV0DoubleManager(size)
    v0CharManager = MemoryV0CharManager(size)

    v1IntManager = MemoryV1IntManager(size)
    v1DoubleManager = MemoryV1DoubleManager(size)
    v1CharManager = MemoryV1CharManager(size)

    v3IntManager = MemoryV3IntManager(size)
    v3DoubleManager = MemoryV3DoubleManager(size)
    v3CharManager = MemoryV3CharManager(size)

    intPack = generateRunPack(size, Gen.int).run()
    setInitial(intPack, v0IntManager, v1IntManager, v3IntManager)

    doublePack = generateRunPack(size, Gen.double(-1e8, 1e8)).run()
    setInitial(doublePack, v0DoubleManager, v1DoubleManager, v3DoubleManager)

    charPack = generateRunPack(size, Gen.char).run()
    setInitial(charPack, v0CharManager, v1CharManager, v3CharManager)
  }

  @Benchmark
  def getIntMemoryV0(): Unit = intGetBench(v0IntManager)

  @Benchmark
  def getIntMemoryV1(): Unit = intGetBench(v1IntManager)

  @Benchmark
  def getIntMemoryV3(): Unit = intGetBench(v3IntManager)

  @Benchmark
  def getDoubleMemoryV0(): Unit = doubleGetBench(v0DoubleManager)

  @Benchmark
  def getDoubleMemoryV1(): Unit = doubleGetBench(v1DoubleManager)

  @Benchmark
  def getDoubleMemoryV3(): Unit = doubleGetBench(v3DoubleManager)

  @Benchmark
  def getCharMemoryV0(): Unit = charGetBench(v0CharManager)

  @Benchmark
  def getCharMemoryV1(): Unit = charGetBench(v1CharManager)

  @Benchmark
  def getCharMemoryV3(): Unit = charGetBench(v3CharManager)

  @Benchmark
  def setIntMemoryV0(): Unit = intSetBench(v0IntManager)

  @Benchmark
  def setIntMemoryV1(): Unit = intSetBench(v1IntManager)

  @Benchmark
  def setIntMemoryV3(): Unit = intSetBench(v3IntManager)

  @Benchmark
  def setDoubleMemoryV0(): Unit = doubleSetBench(v0DoubleManager)

  @Benchmark
  def setDoubleMemoryV1(): Unit = doubleSetBench(v1DoubleManager)

  @Benchmark
  def setDoubleMemoryV3(): Unit = doubleSetBench(v3DoubleManager)

  @Benchmark
  def setCharMemoryV0(): Unit = charSetBench(v0CharManager)

  @Benchmark
  def setCharMemoryV1(): Unit = charSetBench(v1CharManager)

  @Benchmark
  def setCharMemoryV3(): Unit = charSetBench(v3CharManager)

  private def setMemoryBench[T](
    values: Seq[T],
  )(manager: MemoryResourceManager[T]): Unit =
    values
      .zipWithIndex
      .foreach { case (v, i) =>
        manager.provideSet(v, i)
      }

  private def intSetBench    = setMemoryBench[Int](intPack.valuesToSet)
  private def doubleSetBench = setMemoryBench[Double](doublePack.valuesToSet)
  private def charSetBench   = setMemoryBench[Char](charPack.valuesToSet)

  private def getMemoryBench[T](
    indices: Seq[Int],
  )(manager: MemoryResourceManager[T]): Unit = {
    indices.foreach { i =>
      manager.provideGet(i)
    }
  }

  private def intGetBench    = getMemoryBench[Int](intPack.indices)
  private def doubleGetBench = getMemoryBench[Double](doublePack.indices)
  private def charGetBench   = getMemoryBench[Char](charPack.indices)

  private def setInitial[T](
    pack: ValuesRunPack[T],
    managers: MemoryResourceManager[T]*,
  ): Unit =
    pack
      .initialValues
      .zipWithIndex
      .foreach { case (v, i) =>
        managers.foreach(_.provideSet(v, i))
      }

}
