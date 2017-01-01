package com.github.syamantm.logback.api

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.TimeUnit

/**
  * @author syamantak.
  */

/**
  *
  * @param logger name of the logger
  * @param level log level
  */
case class LoggerLogLevel(logger: String, level: String)


case class AppenderState(logLevels: Seq[LoggerLogLevel])

/**
  *
  * @param externalSourceTimeout a timeout in timeUnit, after which external sources request will timeout, default is 5 MINUTES
  * @param initialDelay          initial delay in timeUnit to start the scheduler, default is 1 MINUTES
  * @param interval              interval between each scheduled task, default is 5 MINUTES
  * @param timeUnit              TimeUnit, default is MINUTES
  */
case class ScheduleConfig(externalSourceTimeout: Long = 5,
                          initialDelay: Long = 1,
                          interval: Long = 5,
                          timeUnit: TimeUnit = TimeUnit.MINUTES)