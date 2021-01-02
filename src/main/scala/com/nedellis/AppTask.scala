package com.nedellis

import com.typesafe.scalalogging.StrictLogging

import java.util.concurrent._

object AppTask extends StrictLogging {
  val ex = new ScheduledThreadPoolExecutor(1)

  object HeartbeatTask {
    def start(): Unit = {
      val task = new Runnable {
        def run(): Unit = logger.info(s"Members: ${AppState.listMembers()}")
      }
      ex.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS)
    }
  }

}
