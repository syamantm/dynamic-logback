package com.github.syamantm.logback.api

import java.util.concurrent.{CountDownLatch, Executors}

import com.github.syamantm.logback.schedule.ChangeLogLevelTask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

sealed trait LoggerChangeScheduler {
  def start(): Unit

  def stop(): Unit
}

/**
  * @author syamantak.
  */
class DefaultLoggerChangeScheduler(source: LoggingConfigSource,
                                   scheduleConfig: ScheduleConfig) extends LoggerChangeScheduler {

  private val scheduler = Executors.newSingleThreadScheduledExecutor()

  def start(): Unit = {
    val task = new Runnable {
      override def run(): Unit = {
        val changeLogLevel = new ChangeLogLevelTask(source)
        val latch = new CountDownLatch(1)
        changeLogLevel() onComplete {
          case Success(_) => latch.countDown()
          case Failure(_) => latch.countDown()
        }
        latch.await(scheduleConfig.externalSourceTimeout, scheduleConfig.timeUnit)
      }
    }
    scheduler.scheduleAtFixedRate(task, scheduleConfig.initialDelay, scheduleConfig.interval, scheduleConfig.timeUnit)
  }

  override def stop(): Unit = scheduler.shutdown()
}



