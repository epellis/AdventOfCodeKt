package com.nedellis

import io.getquill.{LowerCase, SqliteJdbcContext}

object HeartbeatTable extends AppState.DBTable {
  override def CREATE_STMT: String =
    """CREATE TABLE IF NOT EXISTS heartbeat (
      | address text NOT NULL PRIMARY KEY,
      | count integer NOT NULL
      |);
      |""".stripMargin

  case class Heartbeat(address: String, count: Long) {}

}

object KVTable extends AppState.DBTable {
  override def CREATE_STMT: String =
    """CREATE TABLE IF NOT EXISTS kv (
      | key text NOT NULL PRIMARY KEY,
      | value text NOT NULL
      |);
      |""".stripMargin

  case class KV(key: String, value: String) {}

}


object AppState {

  trait DBTable {
    def CREATE_STMT: String
  }

  lazy val ctx = new SqliteJdbcContext(LowerCase, "ctx")

  import ctx._
  import HeartbeatTable.Heartbeat

  ctx.executeAction(HeartbeatTable.CREATE_STMT)
  ctx.executeAction(KVTable.CREATE_STMT)

  def updateHeart(heartbeat: Heartbeat): Unit = {
    val stmt = quote(query[Heartbeat].insert(lift(heartbeat)))
    ctx.run(stmt)
  }

  def listHearts(): List[Heartbeat] = {
    val stmt = quote(query[Heartbeat].filter(_ => true))
    ctx.run(stmt)
  }
}
