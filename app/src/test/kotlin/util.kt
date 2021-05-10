package com.github.mpetuska.shak.util

import org.kodein.di.DI

suspend fun <T> withDI(vararg modules: DI.Module, block: suspend DI.() -> T): T =
  block.invoke(DI { importAll(*modules, allowOverride = true) }).also {
    System.gc()
  }
