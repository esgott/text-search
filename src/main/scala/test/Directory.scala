package test

import java.nio.file.{FileSystems, Files, Path}
import scala.jdk.CollectionConverters._
import scala.util.Try
import scala.io.Source

case class Directory(files: List[Iterator[String]])

object Directory {

  def apply(path: String): Either[ReadFileError, Directory] =
    for {
      files    <- listDirectory(path)
      iterators = files.map(_.toFile).map(Source.fromFile).map(_.getLines)
    } yield Directory(iterators)

  private def listDirectory(path: String): Either[ReadFileError, List[Path]] =
    Try {
      val dir = FileSystems.getDefault.getPath(path)
      Files.walk(dir, 1).iterator.asScala.filter(Files.isRegularFile(_)).toList
    }.toEither.left.map(ReadFileError.DirectoryNotFound.apply)

}
