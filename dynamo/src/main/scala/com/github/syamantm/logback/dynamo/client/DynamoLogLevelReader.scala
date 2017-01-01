package com.github.syamantm.logback.dynamo.client

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import com.github.syamantm.logback.api.LoggerLogLevel

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}

case class DynamoDBConfig(tableName: String,
                          loggerKeyName: String,
                          levelKeyName: String)

case class DynamoLogLevelReader(config: DynamoDBConfig, awsDynamoClient: AmazonDynamoDBClient) {

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