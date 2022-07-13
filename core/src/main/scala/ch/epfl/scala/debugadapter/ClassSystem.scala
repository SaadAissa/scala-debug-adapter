package ch.epfl.scala.debugadapter

import ch.epfl.scala.debugadapter.internal.IO

import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Files

sealed trait ClassSystem {
  def within[T](f: (FileSystem, Path) => T): Option[T]
  def readBytes(path: String): Array[Byte] =
    within { (_, root) =>
      Files.readAllBytes(root.resolve(path))
    }.get
}

final case class ClassJar(absolutePath: Path) extends ClassSystem {
  def within[T](f: (FileSystem, Path) => T): Option[T] =
    IO.withinJarFile(absolutePath)(fs => f(fs, fs.getPath("/")))
}

final case class ClassDirectory(absolutePath: Path) extends ClassSystem {
  def within[T](f: (FileSystem, Path) => T): Option[T] =
    Some(f(FileSystems.getDefault, absolutePath))
}

final case class JavaRuntimeSystem(classLoader: ClassLoader, javaHome: Path)
    extends ClassSystem {
  def within[T](f: (FileSystem, Path) => T): Option[T] = {
    Some(
      IO.withinJavaRuntimeFileSystem(classLoader, javaHome)(fs =>
        f(fs, fs.getPath("/modules"))
      )
    )
  }
}
