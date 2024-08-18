rootProject.name = "minecraft"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenCentral()
        maven("https://mvn.bladehunt.net/releases")
        maven("https://jitpack.io")
    }
}
