package com.nedellis

import com.twitter.finagle.Http
import com.twitter.util.Await

object AppServerMain extends AppServer

class AppServer extends App {
  val service = new AppService()
  val server = Http.serve(":8888", service)
  Await.ready(server)
}
