package com.nedellis

import com.typesafe.scalalogging.StrictLogging

import java.util.concurrent._
import scala.util.Random

class AppTasks(state: AppState) {
  val ex = new ScheduledThreadPoolExecutor(1)

  val heartbeatTask = new HeartbeatTask(state)

  heartbeatTask.startScheduleTask(ex)
}

trait AppTask {
  def periodSeconds: Int

  def runTask(): Unit

  def startScheduleTask(ex: ScheduledThreadPoolExecutor): Unit = {
    val taskRunnable = new Runnable {
      override def run(): Unit = runTask()
    }

    ex.scheduleAtFixedRate(taskRunnable, 0, periodSeconds, TimeUnit.SECONDS)
  }
}

class HeartbeatTask(state: AppState) extends AppTask with StrictLogging {
  override def periodSeconds: Int = 3

  override def runTask(): Unit = {
    val currentHeartbeat = state.heartbeatTable.getSelfHeartbeat
    state.heartbeatTable.updateHeartbeat(currentHeartbeat.copy(count = currentHeartbeat.count + 1))

    logger.info(s"CurrentHeartbeat: $currentHeartbeat")

    val heartbeats = state.heartbeatTable.getHeartbeats
    if (heartbeats.nonEmpty) {
      val neighbor = heartbeats(Random.nextInt(heartbeats.size))
      logger.info(s"Heartbeating to $neighbor")
    }
  }
}
