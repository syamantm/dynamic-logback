package com.github.syamantm.logback.api

import java.util.concurrent.{CountDownLatch, Executors}

import com.github.syamantm.logback.schedule.ChangeLogLevelTask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * @author syamantak.
  */
class LoggerChangeScheduler(externalSource: ExternalSource, scheduleConfig: ScheduleConfig) {

  private val scheduler = Executors.newSingleThreadScheduledExecutor()

  def start(): Unit = {
    val task = new Runnable {
      override def run(): Unit = {
        val changeLogLevel = new ChangeLogLevelTask(externalSource)
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
}