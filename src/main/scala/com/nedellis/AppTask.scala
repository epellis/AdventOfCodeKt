package com.nedellis

import com.typesafe.scalalogging.StrictLogging

import java.util.concurrent._

trait AppTaskTrait extends StrictLogging {
  def periodSeconds: Int

  def run(): Unit
}

object HeartbeatTask extends AppTaskTrait {
  override def periodSeconds: Int = 3

  override def run(): Unit = {
    logger.info(s"Echoing to members: ${AppState.listMembers()}")
  }
}

object AppTask extends StrictLogging {
  val ex = new ScheduledThreadPoolExecutor(1)

  def startScheduleTask(task: AppTaskTrait): Unit = {
    val taskRunnable = new Runnable {
      override def run(): Unit = task.run()
    }
    ex.scheduleAtFixedRate(taskRunnable, 0, task.periodSeconds, TimeUnit.SECONDS)
  }
}
