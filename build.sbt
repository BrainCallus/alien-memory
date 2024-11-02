import sbt.Keys.scalacOptions

import scala.collection.Seq
import pl.project13.scala.sbt.JmhPlugin


val scala213Version = "2.13.14"

ThisBuild / organization := "com.yandex.classifieds"
ThisBuild / version      := "0.1.0"
ThisBuild / licenses :=
  List("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / publishMavenStyle := true
ThisBuild / homepage :=
  Some(url("https://github.com/YandexClassifieds/alien-memory"))
ThisBuild / developers :=
  List(
    Developer(
      "lishy2",
      "Islam Talipov",
      "lishy2@yandex-team.ru",
      url("https://github.com/lishy2"),
    ),
  )

ThisBuild / scmInfo :=
  Some(
    ScmInfo(
      url("https://github.com/YandexClassifieds/alien-memory"),
      "https://github.com/YandexClassifieds/alien-memory.git",
    ),
  )
ThisBuild / publishTo := Some(Opts.resolver.sonatypeStaging)

lazy val zioVersion         = "2.1.6"
lazy val supertaggedVersion = "2.0-RC2"

lazy val zioTest      = "dev.zio"       %% "zio-test"      % zioVersion % Test
lazy val zioTestSbt   = "dev.zio"       %% "zio-test-sbt"  % zioVersion % Test
lazy val scalaReflect = "org.scala-lang" % "scala-reflect" % scala213Version
lazy val supertagged  = "org.rudogma"   %% "supertagged"   % supertaggedVersion

lazy val memoryDependencies =
  libraryDependencies ++= Seq(supertagged, scalaReflect, zioTest, zioTestSbt)

val compilerOptions = Seq(
  "-Xsource:3",
  "-deprecation",
  "-encoding",
  "utf-8",
  "-explaintypes",
  "-Vimplicits",
  "-Vtype-diffs",
  "-feature",
  "-language:existentials",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-Wconf:cat=scala3-migration:silent",
)

lazy val alienMemory = (project in file("memory")).settings(
  name         := "alien-memory",
  scalaVersion := scala213Version,
  scalacOptions ++= compilerOptions,
  memoryDependencies,
)

lazy val alienMemory0 = (project in file("memory_v0")).settings(
  name         := "alien-memory-0",
  scalaVersion := scala213Version,
  scalacOptions ++= compilerOptions,
  memoryDependencies,
)

lazy val alienMemory3 = (project in file("memory_v3")).settings(
  name         := "alien-memory-3",
  scalaVersion := scala213Version,
  scalacOptions ++= compilerOptions,
  memoryDependencies,
)

lazy val alienExamples = (project in file("examples"))
  .settings(
    name         := "alien-examples",
    scalaVersion := scala213Version,
    scalacOptions ++= compilerOptions,
    publish / skip := true,
    doc / skip     := true,
  )
  .dependsOn(alienMemory)

lazy val alienJmh = (project in file("alienJmh")).settings(
  name := "alienJmh", libraryDependencies ++= Seq(supertagged, scalaReflect, zioTest, zioTestSbt),
  scalaVersion := scala213Version,
  scalacOptions ++= compilerOptions,
  publish / skip := true,
  doc / skip     := true,
  Jmh / javaOptions += s"-Djava.library.path=$asyncHome/lib",
  Jmh / sourceDirectory :=
    (Test / sourceDirectory).value,
  Jmh / classDirectory :=
    (Test / classDirectory).value,
  Jmh / dependencyClasspath :=
    (Test / dependencyClasspath).value,
  Jmh / compile :=
    (Jmh / compile).dependsOn(Test / compile).value,
  Jmh / run :=
    (Jmh / run).dependsOn(Jmh / compile).evaluated,
)
.dependsOn(alienMemory,alienMemory0, alienMemory3)
 .enablePlugins(JmhPlugin)

lazy val root = (project in file("."))
  .aggregate(alienMemory, alienExamples, alienJmh,alienMemory0, alienMemory3)
  .settings(publish / skip := true, doc / skip := true)


lazy val boiler = taskKey[Unit]("Generates boilerplate")

boiler := {
  HandleBoiler.generate("memory/")
}

lazy val asyncHome = sys
  .env
  .getOrElse(
    "ASYNC_PROFILER_HOME",
    "ASYNC_PROFILER_HOME_env_not_specified",
    //    throw new RuntimeException(
    //      "Please setup 'ASYNC_PROFILER_HOME' env as described in 'benchmarks.md'",
    //    )
  )

