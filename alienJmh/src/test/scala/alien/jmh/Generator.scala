package alien.jmh

import zio.Unsafe
import zio.test.Gen

/**
 * Генерирует документы, запросы, битсеты и сегменты.
 *
 * Методы могут вызываться как в ZIO контексте,
 * так и без него (запуск собственного рантайма).
 */
object Generator {

  def generateRunPack[T](n: Int, gen: Gen[Any, T]): Gen[Any, ValuesRunPack[T]] =
    for {
      indices     <- generatePermutedRange(n)
      initial     <- Gen.listOfBounded(n, n)(gen)
      valuesToSet <- Gen.listOfBounded(n, n)(gen)
    } yield ValuesRunPack(indices, initial, valuesToSet)

  def generatePermutedRange(n: Int): Gen[Any, List[Int]] =
    Gen.const((0 until n).toSet.toList)

  implicit class GenMaterialize[T](gen: Gen[Any, T]) {

    def run(): T = {
      val runtime = zio.Runtime.default
      Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(gen.runHead.map(_.get)).getOrThrowFiberFailure()
      }
    }

    def runToList(n: Int): List[T] = {
      val runtime = zio.Runtime.default
      Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(gen.runCollectN(n)).getOrThrowFiberFailure()
      }
    }

  }

  case class ValuesRunPack[T](
      indices: Seq[Int],
      initialValues: Seq[T],
      valuesToSet: Seq[T],
  )

}
