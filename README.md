dynamic-logback
===============
dynamically change logback log level at runtime.

## Overview
Often time there is a need to change the log level on a running jvm, without restarting the application. This library
implements a way to make it possible to change logback log level at runtime based on the config on an external source.

## Modules
* `dynamic-logback-core`: Contains the core implementation to change the log level at run time, based on an abstract external source.
* `dynamic-logback-dynamo`: implements [AWS DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html) as an external source.

## Usage

```scala
// boilerplate for DynamoDB
val dynamoDbConfig = DynamoDBConfig(...)
val dynamoDbClient = new AmazonDynamoDBClient(new DefaultAWSCredentialsProviderChain)
val reader = DynamoLogLevelReader(dynamoDbConfig, dynamoDbClient)
 
// Configure an external source 
val source = DynamoDBSource(reader)

val config = ScheduleConfig(..)
val scheduler = new LoggerChangeScheduler(source, config)

// start the scheduler
scheduler.start()
```