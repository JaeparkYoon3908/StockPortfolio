plugins {
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}

buildscript {
    repositories {
        maven(url="https://jitpack.io")
        maven(url="https://mvnrepository.com/artifact/com.github.gundy/semver4j")
        mavenCentral()
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

allprojects {
    repositories {
        maven(url="https://jitpack.io")
        maven(url="https://mvnrepository.com/artifact/com.github.gundy/semver4j")
        mavenCentral()
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}