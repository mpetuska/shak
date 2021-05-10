package com.github.mpetuska.shak.repository

import com.github.mpetuska.shak.config.dataSources
import com.github.mpetuska.shak.model.Message
import com.github.mpetuska.shak.model.MessagesTable
import com.github.mpetuska.shak.util.sha256
import com.github.mpetuska.shak.util.withDI
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.instance

class MessageRepositoryTest : ShouldSpec({
  beforeSpec {
    System.setProperty("test", "true")
  }
  afterSpec {
    System.clearProperty("test")
  }
  afterTest {
    transaction {
      MessagesTable.deleteAll()
    }
  }

  val sample = "Hello there!"
  val (sha, msg) = Message(sample.sha256(), sample)
  context("find") {
    should("return a message when found") {
      withDI(dataSources) {
        val db by instance<Database>()
        val target = MessageRepository(db)
        target.save(sha, msg)
        target.find(sha) shouldBe Message(sha, msg)
      }
    }
    should("return null when not found") {
      withDI(dataSources) {
        val db by instance<Database>()
        val target = MessageRepository(db)
        target.find(sha) shouldBe null
      }
    }
  }

  context("save") {
    should("save and return a message") {
      withDI(dataSources) {
        val db by instance<Database>()
        val target = MessageRepository(db)
        target.save(sha, msg) shouldBe Message(sha, msg)
        target.find(sha) shouldBe Message(sha, msg)
      }
    }
  }

  context("delete") {
    should("delete a message and return 1 when it exists") {
      withDI(dataSources) {
        val db by instance<Database>()
        val target = MessageRepository(db)
        target.save(sha, msg)
        target.delete(sha) shouldBeExactly 1
        target.find(sha) shouldBe null
      }
    }
    should("return 0 if the message does not exist") {
      withDI(dataSources) {
        val db by instance<Database>()
        val target = MessageRepository(db)
        target.find(sha) shouldBe null
        target.delete(sha) shouldBeExactly 0
      }
    }
  }
})
