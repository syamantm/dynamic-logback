package com.github.syamantm.logback.dynamo

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.github.syamantm.logback.api.LoggingConfigSource

/**
  * @author syamantak.
  */
class DynamoDBSourceBuilder {
  private var config =  DynamoDBConfig()
  private var awsDynamoClient = new AmazonDynamoDBClient(new DefaultAWSCredentialsProviderChain)

  def usingConfig(config: DynamoDBConfig): DynamoDBSourceBuilder = {
    this.config = config
    this
  }

  def usingDynamoDBClient(client: AmazonDynamoDBClient): DynamoDBSourceBuilder = {
    this.awsDynamoClient = client
    this
  }

  def toSource(): LoggingConfigSource = DynamoDBSource(config, awsDynamoClient)
}

object DynamoDBSourceBuilder {
  def apply: DynamoDBSourceBuilder = new DynamoDBSourceBuilder()
}