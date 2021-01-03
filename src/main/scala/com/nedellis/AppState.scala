package com.nedellis

import scalikejdbc._

case class Heartbeat(address: String, count: Long) {}

case class KV(key: String, value: String) {}

class HeartbeatTable(implicit override val session: AutoSession) extends DBTable {
  override def CREATE_STMT: SQLExecution =
    sql"""CREATE TABLE IF NOT EXISTS heartbeat (
         | address text NOT NULL PRIMARY KEY,
         | count integer NOT NULL
         |);
         |""".stripMargin.execute()

  object Heartbeat extends SQLSyntaxSupport[Heartbeat] {
    override val tableName = "heartbeat"

    def apply(rs: WrappedResultSet) = new Heartbeat(
      rs.string("address"), rs.long("count")
    )
  }

  def updateHeartbeat(heartbeat: Heartbeat): Unit = {
    sql"insert into heartbeat (address, count) values (${heartbeat.address}, ${heartbeat.count})".update().apply()
  }

  def getHeartbeats: List[Heartbeat] = {
    sql"select * from heartbeat".map(h => Heartbeat(h)).list.apply()
  }

  def getHeartbeat(address: String): Option[Heartbeat] = {
    sql"select * from heartbeat where address = $address".map(h => Heartbeat(h)).single().apply()
  }

  def getSelfHeartbeat: Heartbeat = getHeartbeat(AppConfig.ipAddress).get
}

class KVTable(implicit override val session: AutoSession) extends DBTable {
  val HEARTBEAT_KEY = "heartbeat"

  override def CREATE_STMT: SQLExecution =
    sql"""CREATE TABLE IF NOT EXISTS kv (
         | key text NOT NULL PRIMARY KEY,
         | value text NOT NULL
         |);
         |""".stripMargin.execute()

}

abstract class DBTable(implicit val session: AutoSession) {
  def CREATE_STMT: SQLExecution

  CREATE_STMT.apply()
}


class AppState {

  Class.forName("org.sqlite.JDBC")
  ConnectionPool.singleton("jdbc:sqlite::memory:", null, null)

  private implicit val session = AutoSession

  val heartbeatTable = new HeartbeatTable()
  val kvTable = new KVTable()

  heartbeatTable.updateHeartbeat(Heartbeat(AppConfig.ipAddress, 0L))
}
