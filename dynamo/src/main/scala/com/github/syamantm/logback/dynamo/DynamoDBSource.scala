package com.github.syamantm.logback.dynamo

import com.github.syamantm.logback.api.{AppenderState, ExternalSource}
import com.github.syamantm.logback.dynamo.client.DynamoLogLevelReader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author syamantak.
  */
case class DynamoDBSource(reader: DynamoLogLevelReader) extends ExternalSource {
  override def getAppenderState(): Future[AppenderState] = reader.getDynamoConfig() map { config =>
    AppenderState(logLevels = config)
  }
}
