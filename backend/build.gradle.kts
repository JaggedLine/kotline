plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.5.0")
    implementation("io.ktor:ktor-server-netty:1.5.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-serialization:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")

    testImplementation("io.ktor:ktor-server-tests:1.5.0")
}

application {
    mainClassName = "ApplicationKt"
}