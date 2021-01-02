package com.nedellis

import com.typesafe.scalalogging.StrictLogging

import java.util.concurrent._
import scala.util.Random


object HeartbeatTask extends AppTask.AppTaskTrait with StrictLogging {
  override def periodSeconds: Int = 3

  override def run(): Unit = {
    val hearts = AppState.listHearts()
    if (hearts.nonEmpty) {
      val neighbor = hearts(Random.nextInt(hearts.size))
      logger.info(s"Heartbeating to $neighbor")
    }
  }
}

object AppTask {

  trait AppTaskTrait {
    def periodSeconds: Int

    def run(): Unit
  }

  val ex = new ScheduledThreadPoolExecutor(1)

  def startScheduleTask(task: AppTaskTrait): Unit = {
    val taskRunnable = new Runnable {
      override def run(): Unit = task.run()
    }
    ex.scheduleAtFixedRate(taskRunnable, 0, task.periodSeconds, TimeUnit.SECONDS)
  }
}
