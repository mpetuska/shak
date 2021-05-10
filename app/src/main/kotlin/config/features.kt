package com.github.mpetuska.shak.config

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.serialization.json
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
