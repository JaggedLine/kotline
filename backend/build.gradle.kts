plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"
val exposedVersion: String by project

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
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.h2database:h2:1.3.148")
    testImplementation("io.ktor:ktor-server-tests:1.5.0")
}

application {
    mainClassName = "ApplicationKt"
}