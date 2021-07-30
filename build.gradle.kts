group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    id("org.jetbrains.kotlin.js") version "1.4.30" apply(false)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.30" apply(false)
}

repositories {
    mavenCentral()
}
