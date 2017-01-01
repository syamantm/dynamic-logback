package com.github.syamantm.logback.api

import scala.concurrent.Future

/**
  * @author syamantak.
  */
trait ExternalSource {
  def getAppenderState(): Future[AppenderState]
}
