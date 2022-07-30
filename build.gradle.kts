group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("js") version "1.7.0" apply(false)
    kotlin("plugin.serialization") version "1.7.0" apply(false)
}

repositories {
    mavenCentral()
}
