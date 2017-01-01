package com.github.syamantm.logback.schedule

import ch.qos.logback.classic.{Level, Logger => LogbackLogger}
import com.github.syamantm.logback.api.LoggingConfigSource
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author syamantak.
  */
class ChangeLogLevelTask(source: LoggingConfigSource) {

  def apply(): Future[Unit] = {
    source.getLoggerState() map { state =>
      state.logLevels foreach { loggerLogLevel =>
        val level = parseLogLevel(loggerLogLevel.level)
        LoggerFactory.getLogger(loggerLogLevel.logger) match {
          case logger: LogbackLogger => changeIfNeeded(logger, level)
          case _ => //do nothing
        }
      }
    }
  }

  private def parseLogLevel(level: String): Level = {
    level.toUpperCase match {
      case "ERROR" => Level.ERROR
      case "WARN" => Level.WARN
      case "INFO" => Level.INFO
      case "DEBUG" => Level.DEBUG
      case "TRACE" => Level.TRACE
      case "ALL" => Level.ALL
      case _ => Level.ERROR
    }
  }

  private def changeIfNeeded(logger: LogbackLogger, newLevel: Level): Unit = {
    if (!logger.getLevel.equals(newLevel)) {
      logger.setLevel(newLevel)
    }
  }
}
