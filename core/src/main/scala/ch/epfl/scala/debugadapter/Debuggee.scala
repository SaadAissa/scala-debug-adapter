package ch.epfl.scala.debugadapter

import java.nio.file.Path

trait Debuggee {
  def name: String
  def scalaVersion: ScalaVersion
  def run(listener: DebuggeeListener): CancelableFuture[Unit]
  def modules: Seq[Module]
  def libraries: Seq[Library]
  def unmanagedEntries: Seq[UnmanagedEntry]
  def javaRuntime: Option[JavaRuntime]

  def managedEntries: Seq[ManagedEntry] = modules ++ libraries
  def classPathEntries: Seq[ClassPathEntry] = managedEntries ++ unmanagedEntries
  val classPath: Seq[Path] = classPathEntries.map(_.absolutePath)
  val classEntries: Seq[ClassEntry] = classPathEntries ++ javaRuntime
}