package com.nedellis

import .Heartbeat
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class AppController extends Controller {
  AppTask.startScheduleTask(HeartbeatTask)

  get("/") { request: Request =>
    "Hello World"
  }

  post("/heartbeat") { request: Heartbeat =>
    AppState.updateHeart(request.address)
  }
}
