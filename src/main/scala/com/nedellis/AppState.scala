package com.nedellis

import io.getquill.{LowerCase, SqliteJdbcContext}

object MemberTable {
  val CREATE_TABLE_STMT =
    """CREATE TABLE IF NOT EXISTS members (
      | address text
      |);
      |""".stripMargin

  case class Member(id: Int, address: String)

}

class AppState {
  lazy val ctx = new SqliteJdbcContext(LowerCase, "ctx")

  import ctx._

  ctx.executeAction(MemberTable.CREATE_TABLE_STMT)
}
