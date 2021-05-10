package com.github.mpetuska.shak

import com.github.mpetuska.shak.config.di
import com.github.mpetuska.shak.config.installFeatures
import com.github.mpetuska.shak.config.installRouting
import io.ktor.application.*
import org.kodein.di.DI
import org.kodein.di.ktor.DIFeature

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module(vararg diModules: DI.Module = di) {
  installFeatures(*diModules)
  installRouting()
}