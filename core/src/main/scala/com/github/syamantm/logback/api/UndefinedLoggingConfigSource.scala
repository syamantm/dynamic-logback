package com.github.syamantm.logback.api

import scala.concurrent.Future

/**
  * @author syamantak.
  */
class UndefinedLoggingConfigSource extends LoggingConfigSource {
  override def getLoggerState(): Future[LoggerState] = Future.successful(LoggerState(logLevels = Seq.empty))
}
