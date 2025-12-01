plugins {
    id("your.project.android.library")
    id("your.project.android.hilt")
}

android {
    namespace = "com.template.data.repository"
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-common"))
    implementation(project(":data-network"))
    implementation(project(":data-database"))
    implementation(project(":data-datastore"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.compose) // Repository 可能返回 PagingData
}