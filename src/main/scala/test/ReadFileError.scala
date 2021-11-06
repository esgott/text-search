package test

sealed trait ReadFileError

object ReadFileError {

  case object MissingPathArg                 extends ReadFileError
  case class DirectoryNotFound(t: Throwable) extends ReadFileError
  case class NonBinaryFile(fileName: String) extends ReadFileError
  case class ReadingError(t: Throwable)      extends ReadFileError

}
