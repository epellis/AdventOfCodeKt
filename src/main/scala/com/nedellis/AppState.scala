package com.nedellis

import scalikejdbc._

import AppSchema._

class HeartbeatTable(implicit override val session: AutoSession) extends DBTable {
  override def CREATE_STMT: SQLExecution =
    sql"""CREATE TABLE IF NOT EXISTS heartbeat (
         | address VARCHAR(255) NOT NULL PRIMARY KEY,
         | count integer NOT NULL,
         | epoch integer NOT NULL
         |);
         |""".stripMargin.execute()

  object Heartbeat extends SQLSyntaxSupport[Heartbeat] {
    override val tableName = "heartbeat"

    def apply(rs: WrappedResultSet) = new Heartbeat(
      rs.string("address"), rs.long("count"), rs.long("epoch")
    )
  }

  def currentEpoch: Long = getSelfHeartbeat.epoch

  def updateHeartbeat(heartbeat: Heartbeat): Unit = {
    sql"merge into heartbeat key (address) values (${heartbeat.address}, ${heartbeat.count}, ${heartbeat.epoch})".update().apply()
  }

  def incrementSelfHeartbeat(): Unit = {
    val currentHeartbeat = getSelfHeartbeat
    val updatedHeartbeat = currentHeartbeat.copy(count = currentHeartbeat.count + 1, epoch = currentHeartbeat.epoch + 1)
    updateHeartbeat(updatedHeartbeat)
  }

  def deleteHeartbeats(heartbeats: List[Heartbeat]): Unit = {
    heartbeats.foreach { h =>
      sql"delete from heartbeat where address = ${h.address}".execute().apply()
    }
  }

  def getHeartbeats: List[Heartbeat] = {
    sql"select * from heartbeat".map(h => Heartbeat(h)).list.apply()
  }

  def getExternalHeartbeats: List[Heartbeat] = getHeartbeats.filter(_.address != AppConfig.ipAddress)

  def getHeartbeat(address: String): Option[Heartbeat] = {
    sql"select * from heartbeat where address = $address".map(h => Heartbeat(h)).single().apply()
  }

  def getSelfHeartbeat: Heartbeat = getHeartbeat(AppConfig.ipAddress).get
}

class KVTable(implicit override val session: AutoSession) extends DBTable {
  val HEARTBEAT_KEY = "heartbeat"

  override def CREATE_STMT: SQLExecution =
    sql"""CREATE TABLE IF NOT EXISTS kv (
         | key VARCHAR(255) NOT NULL PRIMARY KEY,
         | value VARCHAR(255) NOT NULL
         |);
         |""".stripMargin.execute()

}

abstract class DBTable(implicit val session: AutoSession) {
  def CREATE_STMT: SQLExecution

  CREATE_STMT.apply()
}


class AppState {

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", null, null)

  private implicit val session: AutoSession.type = AutoSession

  val heartbeatTable = new HeartbeatTable()
  val kvTable = new KVTable()

  heartbeatTable.updateHeartbeat(Heartbeat(AppConfig.ipAddress, 0L, 0L))
}
