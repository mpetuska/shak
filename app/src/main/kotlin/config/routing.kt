package com.github.mpetuska.shak.config

import com.github.mpetuska.shak.service.MessageService
import com.github.mpetuska.shak.util.inject
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.installRouting() = routing {
  route("/messages") {
    post {
      val service by inject<MessageService>()
      val message = call.receiveText()
      call.respond(HttpStatusCode.OK, service.save(message).shA256)
    }
    get("/{sha256}") {
      val service by inject<MessageService>()
      val sha256 = call.parameters["sha256"].toString()
      val message = service.findMessage(sha256)
      message?.let { call.respond(HttpStatusCode.OK, it) } ?: call.respond(
        HttpStatusCode.NotFound,
        "No message found for SHA256: $sha256"
      )
    }
    delete("/{sha256}") {
      val service by inject<MessageService>()
      val sha256 = call.parameters["sha256"].toString()
      if (service.delete(sha256) > 0) {
        call.respond(HttpStatusCode.NoContent)
      } else {
        call.respond(
          HttpStatusCode.NotFound,
          "No message found for SHA256: $sha256"
        )
      }
    }
  }
}