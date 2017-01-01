package com.github.syamantm.logback.api

import scala.concurrent.Future

/**
  * @author syamantak.
  */
trait LoggingConfigSource {
  def getLoggerState(): Future[LoggerState]
}
