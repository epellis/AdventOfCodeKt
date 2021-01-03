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
    state.heartbeatTable.incrementSelfHeartbeat()

    doGarbageCollect()

    doGossip()
  }

  def doGarbageCollect(): Unit = {
    val maxEpochDifference = 2

    val currentEpoch = state.heartbeatTable.currentEpoch
    val externalHeartbeats = state.heartbeatTable.getExternalHeartbeats

    val expiredHeartbeats = externalHeartbeats.filter { h =>
      assert(currentEpoch >= h.epoch)
      currentEpoch - h.epoch > maxEpochDifference
    }

    logger.info(s"Expiring: $expiredHeartbeats")
  }

  def doGossip(): Unit = {
    val heartbeats = state.heartbeatTable.getExternalHeartbeats
    if (heartbeats.nonEmpty) {
      val neighbor = heartbeats(Random.nextInt(heartbeats.size))
      logger.info(s"Heartbeating to $neighbor")
    }
  }
}
