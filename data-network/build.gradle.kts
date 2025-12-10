plugins {
    id("your.project.android.library")
    id("your.project.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.template.data.network"
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-common"))
    implementation(project(":data-datastore"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation("com.github.xuexiangjys:XUpdate:2.1.3")
    implementation("androidx.appcompat:appcompat:1.7.0")

}