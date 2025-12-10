plugins {
    id("your.project.android.feature")
}

android {
    namespace = "com.template.feature.onboarding"
}
dependencies {
    implementation(project(":data-network"))
}