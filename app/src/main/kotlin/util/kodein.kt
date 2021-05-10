package com.github.mpetuska.shak.util

import io.ktor.application.*
import io.ktor.util.pipeline.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.on

inline fun <reified T : Any> PipelineContext<*, ApplicationCall>.inject(tag: String? = null) =
  closestDI().on(context).instance<T>(tag)