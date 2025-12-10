pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

//        maven { url = uri("https://maven.aliyun.com/repository/public") }
//        maven { url = uri("https://maven.aliyun.com/repository/google") }
//        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

includeBuild("build-logic")
rootProject.name = "AndroidCommon"
include(":app")
include(":core-model")
include(":core-ui")
include(":core-common")
include(":core-navigation")
include(":data-network")
include(":data-repository")
include(":feature-home")
include(":data-database")
include(":data-datastore")
include(":feature-login")
include(":feature-map")
include(":app-solver")
include(":feature-solver")
include(":codegen")
include(":codegenlib")
include(":app-generated")
include(":feature-login-atrust")
include(":feature-webview")
include(":feature-setting")
include(":feature-ai")
include(":feature-update")
include(":feature-onboarding")
