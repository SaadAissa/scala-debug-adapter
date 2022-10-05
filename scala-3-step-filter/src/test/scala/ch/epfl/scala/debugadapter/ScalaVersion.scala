package ch.epfl.scala.debugadapter

import coursier._

case class ScalaVersion(value: String) {
  def isScala2: Boolean = value.startsWith("2")
  def isScala3: Boolean = value.startsWith("3")
  def isScala212: Boolean = value.startsWith("2.12")
  def isScala213: Boolean = value.startsWith("2.13")

  def binaryVersion: String = if (isScala3) "3" else if (isScala213) "2.13" else "2.12"

  override def toString: String = value
}

object ScalaVersion {
  val `3.2` = ScalaVersion("3.2.0")
}