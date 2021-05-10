package com.github.mpetuska.shak.rest

import com.github.mpetuska.shak.model.Message
import com.github.mpetuska.shak.model.MessagesTable
import com.github.mpetuska.shak.module
import com.github.mpetuska.shak.util.sha256
import io.kotest.assertions.ktor.shouldHaveContent
import io.kotest.assertions.ktor.shouldHaveHeader
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.assertions.ktor.shouldNotHaveContent
import io.kotest.assertions.ktor.shouldNotHaveHeader
import io.kotest.core.spec.style.ShouldSpec
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class MessagesITest : ShouldSpec({
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
  fun mockMessage() = transaction {
    MessagesTable.insert {
      it[sha256] = sha
      it[message] = msg
    }
  }
  context("GET") {
    should("return 200 with a message when it exists") {
      withTestApplication({ module() }) {
        mockMessage()
        handleRequest(HttpMethod.Get, "/messages/$sha").run {
          response shouldHaveStatus HttpStatusCode.OK
          response shouldHaveContent msg
          response.shouldHaveHeader(name = "Content-Type", value = "text/plain; charset=UTF-8")
        }
      }
    }
    should("return 404 when a message doesn't exist") {
      withTestApplication({ module() }) {
        handleRequest(HttpMethod.Get, "/messages/$sha").run {
          response shouldHaveStatus HttpStatusCode.NotFound
          response shouldNotHaveContent msg
          response.shouldHaveHeader(name = "Content-Type", value = "text/plain; charset=UTF-8")
        }
      }
    }
  }

  context("POST") {
    should("return 200 with a SHA256 when it exists") {
      withTestApplication({ module() }) {
        mockMessage()
        handleRequest(HttpMethod.Post, "/messages") {
          setBody(msg)
        }.run {
          response shouldHaveStatus HttpStatusCode.OK
          response shouldHaveContent sha
          response.shouldHaveHeader(name = "Content-Type", value = "text/plain; charset=UTF-8")
        }
      }
    }
    should("save and return 201 with a SHA256 when it doesn't exist") {
      withTestApplication({ module() }) {
        handleRequest(HttpMethod.Post, "/messages") {
          setBody(msg)
        }.run {
          response shouldHaveStatus HttpStatusCode.Created
          response shouldHaveContent sha
          response.shouldHaveHeader(name = "Content-Type", value = "text/plain; charset=UTF-8")
        }
      }
    }
  }

  context("DELETE") {
    should("return 204 when message exists") {
      withTestApplication({ module() }) {
        mockMessage()
        handleRequest(HttpMethod.Delete, "/messages/$sha").run {
          response shouldHaveStatus HttpStatusCode.NoContent
          response shouldNotHaveContent sha
          response.shouldNotHaveHeader(name = "Content-Type", value = "text/plain; charset=UTF-8")
        }
      }
    }
    should("return 404 when message doesn't exist") {
      withTestApplication({ module() }) {
        handleRequest(HttpMethod.Delete, "/messages/$sha").run {
          response shouldHaveStatus HttpStatusCode.NotFound
          response shouldNotHaveContent sha
          response.shouldHaveHeader(name = "Content-Type", value = "text/plain; charset=UTF-8")
        }
      }
    }
  }
})
