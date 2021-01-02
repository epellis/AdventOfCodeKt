package com.nedellis

import com.nedellis.Messages.Advertise
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class AppController extends Controller {
  get("/") { request: Request =>
    "Hello World"
  }

  post("/advertise") { request: Advertise =>
    info(request)
    "Registered"
  }
}
