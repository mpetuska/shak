plugins {
  id("org.jlleitschuh.gradle.ktlint")
  kotlin("jvm") apply false
  idea
}

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

allprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")
  repositories {
    mavenCentral()
  }

  tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
    withType<Test> {
      useJUnitPlatform()
    }
  }
}
