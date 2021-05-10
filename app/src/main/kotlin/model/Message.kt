package com.github.mpetuska.shak.model

import com.github.mpetuska.shak.util.SHA256
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object MessagesTable : Table() {
  val sha256 = varchar("sha256", Int.MAX_VALUE)
  val message = varchar("message", Int.MAX_VALUE)
  override val primaryKey = PrimaryKey(sha256)
}

@Serializable
data class Message(
  val shA256: SHA256,
  val message: String,
) {
  constructor(row: ResultRow) : this(row[MessagesTable.sha256], row[MessagesTable.message])
}
