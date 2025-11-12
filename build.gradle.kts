import com.android.tools.r8.kotlin.S

// File: build.gradle.kts (Project Root)

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
    // Kotlin 序列化插件 (为 codegen 模块准备)
    alias(libs.plugins.kotlin.serialization) apply false
//    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22" apply false
    // kotlin.serialization 插件是编译器插件，不需要在这里声明
}

tasks.register<JavaExec>("generateCode") {
    group = "generation"
    description = "Runs the codegen module to generate source files for the app module."

    // 获取 codegen 模块的 sources asembly
    val codegenProject = project(":codegen")
    val sourceSets = codegenProject.the<org.gradle.api.tasks.SourceSetContainer>()

    // 设置运行所需的 classpath
    // 它应该包含 codegen 模块编译后的类文件以及其所有运行时依赖
    classpath = sourceSets.named("main").flatMap { it.runtimeClasspath as Provider<*> } as FileCollection

    // 告诉任务要执行哪个类的 main 函数
    // 注意：Kotlin 文件 CodeGenerator.kt 编译后对应的类名是 CodeGeneratorKt
    mainClass.set("com.template.codegen.CodeGeneratorKt")

    // (可选) 如果你的 main 函数需要命令行参数，可以在这里设置
    // args("arg1", "arg2")
}
