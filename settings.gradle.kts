pluginManagement {
    repositories {
//        google {
//            content {
//                includeGroupByRegex("com\\.android.*")
//                includeGroupByRegex("com\\.google.*")
//                includeGroupByRegex("androidx.*")
//            }
//        }
        google()
        maven("https://androidx.dev/snapshots/latest/artifacts/repository") // ✅ Use latest snapshot

        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven("https://androidx.dev/snapshots/latest/artifacts/repository")
        mavenCentral()
    }
}

rootProject.name = "dailchampion"
include(":app")
 