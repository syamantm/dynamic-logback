package com.github.syamantm.logback.api

/**
  * @author syamantak.
  */
class DynamicLogging {
  private var source: LoggingConfigSource = new UndefinedLoggingConfigSource
  private var scheduleConfig = ScheduleConfig()

  def fromLoggingConfigSource(externalSource: LoggingConfigSource): DynamicLogging = {
    this.source = externalSource
    this
  }

  def withScheduleConfig(scheduleConfig: ScheduleConfig): DynamicLogging = {
    this.scheduleConfig = scheduleConfig
    this
  }

  def start(): LoggerChangeScheduler = {
    val scheduler  = new DefaultLoggerChangeScheduler(source, scheduleConfig)
    scheduler.start()
    sys.addShutdownHook(scheduler.stop())
    scheduler
  }
}

object DynamicLogging {
  def apply: DynamicLogging = new DynamicLogging()
}