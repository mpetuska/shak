package com.github.mpetuska.shak.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*
import org.kodein.di.DI
import org.kodein.di.ktor.DIFeature

fun Application.installFeatures(vararg diModules: DI.Module) {
  install(CallLogging)
  install(ConditionalHeaders)
  install(CORS) {
    anyHost()
    method(HttpMethod.Get)
    method(HttpMethod.Delete)
    method(HttpMethod.Post)
    method(HttpMethod.Put)
  }
  install(DefaultHeaders) {
    header("X-Engine", "Ktor")
  }
  install(ContentNegotiation) {
    json()
  }
  install(StatusPages) {
    exception<NotFoundException> {
      call.respond(HttpStatusCode.NotFound)
    }
  }
  install(DIFeature) {
    importAll(*diModules)
  }
}