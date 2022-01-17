rootProject.name = "KotlinLine"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        val kotlin_version : String = "1.4.30"

        kotlin("jvm") version kotlin_version
        kotlin("js") version kotlin_version
        kotlin("plugin.serialization") version kotlin_version
    }
}

include("frontend")
include("backend")
