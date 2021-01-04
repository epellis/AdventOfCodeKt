package com.nedellis

import com.twitter.finagle.http.{Fields, Request, Response}
import com.twitter.finagle.{Http, Service, http}
import com.twitter.util.Future

import java.util.concurrent.ConcurrentHashMap

import AppSchema._

class AppClients {
  // TODO: Collect clients if deleted from DB
  //  private val heartbeatClients = new ConcurrentHashMap[String, HeartbeatClient]()
  //
  //  def heartbeatClient(address: String): HeartbeatClient = {
  //    heartbeatClients.computeIfAbsent(address, _ => new HeartbeatClient(address))
  //  }
}

trait AppClient[Req, Res] {
  def endpoint: String

  def send(r: Req): Future[Res]

  def encode(r: Req, address: String): Request = {
    http.Request(http.Method.Post, endpoint)
  }
}

//class HeartbeatClient(address: String) extends AppClient[HeartbeatRequest, HeartbeatResponse] {
////  val client: Service[HeartbeatClient, HeartbeatResponse] = Http.client.newService(address)
//
//  override def endpoint: String = "/heartbeat"
//
////  override def send(r: HeartbeatRequest): Future[HeartbeatResponse] = client(encode(r, address))
//
//  //  def postHeartbeat(h: Heartbeat): Unit = {
//  //    val payload = objectMapper.writeValueAsBuf(h)
//  //
//  //    val r = http.Request(http.Method.Post, "/heartbeat")
//  //    r.host(address)
//  //    r.content = payload
//  //    r.headerMap.add(Fields.ContentLength, payload.length.toString)
//  //    r.headerMap.add("Content-Type", "application/json;charset=UTF-8")
//  //  }
//}
