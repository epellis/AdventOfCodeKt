package com.nedellis

import com.twitter.finagle.{Service, http}
import com.twitter.util.Future
import upickle.default.{read, write}
import AppSchema._
import com.twitter.io.Buf
import com.typesafe.scalalogging.StrictLogging
import scala.util.{Try, Success, Failure}

object AppService {
  val BAD_REQUEST: http.Response = http.Response(http.Status.BadRequest)

  def buildJSONResponse(body: String): http.Response = {
    val r = http.Response(http.Status.Ok)
    r.setContentTypeJson()
    r.setContentString(body)
    r
  }
}

class AppService
  extends Service[http.Request, http.Response]
    with StrictLogging {

  import AppService._

  val state = new AppState()
  val client = new AppClients()
  val tasks = new AppTasks(state, client)

  override def apply(request: http.Request): Future[http.Response] = {
    val r = (request.method, request.uri) match {
      case (http.Method.Get, "/") =>
        buildJSONResponse(write(state.heartbeatTable.getSelfHeartbeat))
      case (http.Method.Post, "/heartbeat") =>
        val Buf.Utf8(str) = request.content
        Try(read[HeartbeatRequest](str)) match {
          case Success(body) =>
            //    val currentEpoch = state.heartbeatTable.currentEpoch
            //    state.heartbeatTable.updateHeartbeat(request.copy(epoch = currentEpoch))
            buildJSONResponse(write(HeartbeatResponse()))
          case Failure(err) =>
            logger.error(s"Tried to decode $str, got error: $err")
            BAD_REQUEST
        }
      case _ => BAD_REQUEST
    }
    Future.value(r)
  }
}
