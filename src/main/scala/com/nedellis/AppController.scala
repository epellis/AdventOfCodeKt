package com.nedellis

import com.nedellis.Messages.Advertise
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class AppController extends Controller {
  val state = new AppState()

  get("/") { request: Request =>
    "Hello World"
  }

  post("/advertise") { request: Advertise =>
    logger.info(s"Advertise: $request")
  }
}
