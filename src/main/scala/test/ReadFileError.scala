package test

sealed trait ReadFileError

object ReadFileError {

  case object MissingPathArg extends ReadFileError

  case class DirectoryNotFound(fileName: String, t: Throwable)
      extends ReadFileError

  case class InvalidTextFile(fileName: String, t: Throwable)
      extends ReadFileError

  case class ReadingError(fileName: String, t: Throwable) extends ReadFileError

}
