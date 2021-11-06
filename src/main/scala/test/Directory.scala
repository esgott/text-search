package test

import test.Directory.File

import java.nio.file.{FileSystems, Files, Path}
import scala.jdk.CollectionConverters._
import scala.util.Try
import scala.io.Source

case class Directory(files: List[File]) {

  def using[T](f: Directory => T): T =
    try {
      f(this)
    } finally {
      close()
    }

  def close(): Unit = files.foreach(_.source.close())

}

object Directory {

  case class File(name: String, source: Source)

  object File {

    def apply(path: Path): File =
      File(
        name = path.getFileName.toString,
        source = Source.fromFile(path.toFile)
      )

  }

  def apply(path: String): Either[ReadFileError, Directory] =
    for {
      files <- listDirectory(path)
    } yield Directory(files.map(File.apply))

  private def listDirectory(path: String): Either[ReadFileError, List[Path]] =
    Try {
      val dir = FileSystems.getDefault.getPath(path)
      Files.walk(dir, 1).iterator.asScala.filter(Files.isRegularFile(_)).toList
    }.toEither.left.map(ReadFileError.DirectoryNotFound.apply)

}
