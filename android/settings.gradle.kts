pluginManagement {
    repositories {
        maven(url="https://jitpack.io")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url="https://jitpack.io")
        google()
        mavenCentral()
        gradlePluginPortal()
    }

}


rootProject.name = "StockPortfolio"
include(":app")
include(":core:network")
include(":core:data")
include(":core:database")
include(":core:datastore")
