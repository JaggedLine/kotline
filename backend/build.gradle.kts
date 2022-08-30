plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.jetbrains.exposed:exposed-core:0.39.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.39.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.39.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0")
    implementation("org.jetbrains.kotlin:kotlin-test:1.7.10")
    implementation("io.ktor:ktor-server-core:2.1.0")
    implementation("io.ktor:ktor-server-netty:2.1.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.1.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.0")
    testImplementation("io.ktor:ktor-server-tests:2.1.0")
}

application {
    mainClass.set("ApplicationKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "15"
    }
}
