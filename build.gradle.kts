@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
}

buildscript {
    repositories {
        maven(url="https://jitpack.io")
        mavenCentral()
        google()
    }

    dependencies {
        classpath(libs.kotlin.serialization)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.gradle)
        classpath(libs.google.services)
        classpath(libs.hilt.android.gradle.plugin)
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}