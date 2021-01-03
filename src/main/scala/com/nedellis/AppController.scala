package com.nedellis

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class AppController extends Controller {
  val state = new AppState()
  val tasks = new AppTasks(state)

  get("/") { request: Request =>
    "Hello World"
  }

  post("/heartbeat") { request: Heartbeat =>
    state.heartbeatTable.updateHeartbeat(request)
  }
}
