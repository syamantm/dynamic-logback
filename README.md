dynamic-logback
===============
dynamically change logback log level at runtime.

## Overview
Often time there is a need to change the log level on a running jvm, without restarting the application. This library
implements a way to make it possible to change logback log level at runtime based on logger configuration defined on an external logging configuration source.

#### Logging Configuration Source 
A configuration source where logging configuration can be changed independently. Typically this would be a remote kv store, such as dynamodb, but the core api makes no
such assumptions.

Coincidentally the main interface is named as [LoggingConfigSource](core/src/main/scala/com/github/syamantm/logback/api/LoggingConfigSource.scala), which returns a Future of `LoggerState`, which encapsulate the state of the logger config on the config source.

## Modules
* `dynamic-logback-core`: Contains the core implementation to change the log level at run time, based on an abstract external source.
* `dynamic-logback-dynamo`: implements [AWS DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html) as an [LoggingConfigSource](core/src/main/scala/com/github/syamantm/logback/api/LoggingConfigSource.scala).

## Usage

### Fluent API

```scala
val loggingConfigSource = DynamoDBSourceBuilder()
                        .usingConfig(DynamoDBConfig(...))
                        .toSource()
                        
val scheduler = DynamicLogging()
                     .fromLoggingConfigSource(loggingConfigSource)
                     .withScheduleConfig(ScheduleConfig(..))
                     .start()
```

### Using Dependency Injection

```scala
// boilerplate for DynamoDB
val dynamoDbConfig = DynamoDBConfig(...)
val dynamoDbClient = new AmazonDynamoDBClient(new DefaultAWSCredentialsProviderChain)
 
// Create a LoggingConfigSource 
val source: LoggingConfigSource = DynamoDBSource(dynamoDbConfig, dynamoDbClient)

val config = ScheduleConfig(..)
val scheduler = new DefaultLoggerChangeScheduler(source, config)

// start the scheduler
scheduler.start()
```