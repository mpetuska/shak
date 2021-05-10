package com.github.mpetuska.shak.db.migration

import com.github.mpetuska.shak.model.MessagesTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("ClassName")
class V1__Create_message_table : BaseJavaMigration() {
  override fun migrate(context: Context?) {
    transaction {
      SchemaUtils.create(MessagesTable)
    }
  }
}
