package com.nedellis

import upickle.default.{macroRW, ReadWriter => RW}
import upickle.default.write

object AppSchema {

  case class Heartbeat(address: String, count: Long, epoch: Long) {}

  object Heartbeat {
    implicit val rw: RW[Heartbeat] = macroRW
  }

  case class KV(key: String, value: String) {}

  case class HeartbeatRequest(heartbeats: List[Heartbeat])

  object HeartbeatRequest {
    implicit val rw: RW[HeartbeatRequest] = macroRW
  }

  case class HeartbeatResponse()

  object HeartbeatResponse {
    implicit val rw: RW[HeartbeatResponse] = macroRW
  }

}
