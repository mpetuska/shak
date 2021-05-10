package com.github.mpetuska.shak.config

import com.github.mpetuska.shak.service.MessageService
import com.github.mpetuska.shak.util.inject
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.installRouting() = routing {
  route("/messages") {
    post {
      val service by inject<MessageService>()
      val message = call.receiveText()
      val (sha, created) = service.save(message)
      call.respond(if (created) HttpStatusCode.Created else HttpStatusCode.OK, sha.shA256)
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
