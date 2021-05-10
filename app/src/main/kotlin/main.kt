package com.github.mpetuska.shak

import com.github.mpetuska.shak.config.di
import com.github.mpetuska.shak.config.installFeatures
import com.github.mpetuska.shak.config.installRouting
import io.ktor.application.Application
import org.kodein.di.DI

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused")
fun Application.module(vararg diModules: DI.Module = di) {
  installFeatures(*diModules)
  installRouting()
}
