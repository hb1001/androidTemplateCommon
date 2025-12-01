plugins {
    id("your.project.android.application")
    id("your.project.android.compose")
    id("your.project.android.hilt")
}

android {
    namespace = "com.template.app.solver"
    defaultConfig {
        applicationId = "com.template.app.solver"
    }
}

dependencies {
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))
    implementation(project(":data-network"))
    implementation(project(":data-repository"))
    implementation(project(":feature-solver"))

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
}