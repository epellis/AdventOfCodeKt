package com.nedellis

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{LoggingMDCFilter, TraceIdMDCFilter, CommonFilters}
import com.twitter.finatra.http.routing.HttpRouter

object AppServerMain extends AppServer

class AppServer extends HttpServer {
  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[AppController]
  }
}
