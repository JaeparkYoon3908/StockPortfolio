pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url="https://jitpack.io")
        maven(url="https://mvnrepository.com/artifact/com.github.gundy/semver4j")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "StockPortfolio"
include(":app")
include(":core:network")
include(":core:data")
