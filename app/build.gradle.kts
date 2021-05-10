plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  application
}

kotlin {
  sourceSets {
    main {
      dependencies {
        implementation("io.ktor:ktor-server-cio:_")
        implementation("ch.qos.logback:logback-classic:_")
        implementation("io.ktor:ktor-serialization:_")
        implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:_")
        implementation("org.jetbrains.exposed:exposed-jdbc:_")
        implementation("com.h2database:h2:_")
        implementation("com.zaxxer:HikariCP:_")
        implementation("org.flywaydb:flyway-core:_")
      }
    }
    test {
      kotlin.srcDir("src/test/unit")
      kotlin.srcDir("src/test/integration")
      dependencies {
        implementation("io.ktor:ktor-server-tests:_")
        implementation("io.kotest:kotest-runner-junit5:_")
        implementation("io.kotest.extensions:kotest-assertions-ktor:_")
      }
    }
  }
}

application {
  mainClass.set("io.ktor.server.cio.EngineMain")
}
