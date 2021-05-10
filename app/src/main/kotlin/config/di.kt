package com.github.mpetuska.shak.config

import com.github.mpetuska.shak.repository.MessageRepository
import com.github.mpetuska.shak.service.MessageService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.eagerSingleton
import org.kodein.di.instance
import org.kodein.di.ktor.CallScope
import org.kodein.di.provider
import org.kodein.di.scoped
import org.kodein.di.singleton
import java.io.File
import java.util.logging.Logger
import javax.sql.DataSource

val dataSources = DI.Module("dataSources") {
  bind<DataSource>("in-memory") {
    provider {
      HikariConfig().apply {
        jdbcUrl = "jdbc:h2:mem:main"
        if (System.getProperty("test", "false") != "true") {
          System.getenv("DB_FILE")?.let { jdbcUrl = "jdbc:h2:file:${File(it).absolutePath}" }
        }
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
  if (System.getProperty("test", "false") != "true") {
    bind("h2-console") {
      eagerSingleton {
        org.h2.tools.Server.createWebServer(
          "-webPort", "8082",
        ).start()
        Logger.getAnonymousLogger().info("H2 Web Console running at http://localhost:8082")
      }
    }
  }
}

val di = arrayOf(dataSources, repositories, services)
