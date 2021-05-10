package com.github.mpetuska.shak.repository

import com.github.mpetuska.shak.model.Message
import com.github.mpetuska.shak.model.MessagesTable
import com.github.mpetuska.shak.util.SHA256
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MessageRepository(
  private val database: Database,
) {
  suspend fun find(sha256: String): Message? = newSuspendedTransaction(db = database) {
    MessagesTable.select { MessagesTable.sha256 eq sha256 }
      .firstOrNull()
      ?.let(::Message)
  }

  suspend fun save(sha256: SHA256, message: String): Message = newSuspendedTransaction(db = database) {
    find(sha256) ?: MessagesTable.insert {
      it[this.sha256] = sha256
      it[this.message] = message
    }.resultedValues!!.first().let(::Message)
  }

  suspend fun delete(sha256: SHA256): Int = newSuspendedTransaction(db = database) {
    MessagesTable.deleteWhere { MessagesTable.sha256 eq sha256 }
  }
}