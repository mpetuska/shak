package com.github.mpetuska.shak.config

import com.github.mpetuska.shak.repository.MessageRepository
import com.github.mpetuska.shak.service.MessageService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.kodein.di.*
import org.kodein.di.ktor.CallScope
import javax.sql.DataSource


val dataSources = DI.Module("dataSources") {
  bind<DataSource>("in-memory") {
    provider {
      HikariConfig().apply {
        jdbcUrl = "jdbc:h2:mem:main"
        driverClassName = "org.h2.Driver"
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        username = "sa"
        validate()
      }.let(::HikariDataSource)
    }
  }

  bind {
    eagerSingleton {
      val dataSource = instance<DataSource>("in-memory")
      Database.connect(dataSource).also {
        Flyway.configure()
          .dataSource(dataSource)
          .locations("com/github/mpetuska/shak/db/migration")
          .load().migrate()
      }
    }
  }
}

val repositories = DI.Module("repositories") {
  bind {
    scoped(CallScope).singleton { MessageRepository(instance()) }
  }
}

val services = DI.Module("services") {
  bind {
    scoped(CallScope).singleton {
      MessageService(instance())
    }
  }
  bind("h2-console") {
    eagerSingleton {
      org.h2.tools.Server.createWebServer(
        "-webPort", "8082",
      ).start()
    }
  }
}

val di = arrayOf(dataSources, repositories, services)