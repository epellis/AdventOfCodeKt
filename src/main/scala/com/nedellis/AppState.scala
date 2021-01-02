package com.nedellis

import io.getquill.{LowerCase, SqliteJdbcContext}

object MemberTable {
  val CREATE_TABLE_STMT: String =
    """CREATE TABLE IF NOT EXISTS member (
      | id integer NOT NULL PRIMARY KEY,
      | address text NOT NULL
      |);
      |""".stripMargin

  case class Member(id: Int, address: String)

}

object AppState {
  lazy val ctx = new SqliteJdbcContext(LowerCase, "ctx")

  import ctx._
  import MemberTable.Member

  ctx.executeAction(MemberTable.CREATE_TABLE_STMT)

  def insertMember(address: String): Unit = {
    val stmt = quote(query[Member].insert(_.address -> lift(address)))
    ctx.run(stmt)
  }

  def listMembers(): List[Member] = {
    val stmt = quote(query[Member].filter(_ => true))
    ctx.run(stmt)
  }
}
