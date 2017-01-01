package com.github.syamantm.logback.dynamo

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import com.github.syamantm.logback.api.{AppenderState, ExternalSource, LoggerLogLevel}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}

/**
  * @author syamantak.
  */

case class DynamoDBConfig(tableName: String = "dynamic-logback",
                          loggerKeyName: String = "logger_name",
                          levelKeyName: String = "log_level")

case class DynamoDBSource(config: DynamoDBConfig, awsDynamoClient: AmazonDynamoDBClient) extends ExternalSource {
  override def getAppenderState(): Future[AppenderState] = getDynamoConfig() map { config =>
    AppenderState(logLevels = config)
  }

  def getDynamoConfig(): Future[Seq[LoggerLogLevel]] = {
    Future {
      blocking {
        readFromDynamoDB()
      }
    }
  }

  private def readFromDynamoDB(): Seq[LoggerLogLevel] = {
    val scanRequest = new ScanRequest(config.tableName)
    val result = awsDynamoClient.scan(scanRequest)
    result.getItems.asScala map { row =>
      val loggerName = row.asScala(config.loggerKeyName).getS
      val level = row.asScala(config.levelKeyName).getS
      LoggerLogLevel(logger = loggerName, level = level)
    }
  }
}



