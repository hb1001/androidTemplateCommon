package com.template.codegen

// codegen/src/main/kotlin/CodeGenerator.kt

import com.squareup.kotlinpoet.*
import java.io.File

/**
 * 主函数，代码生成的入口。
 * 当运行此 main 函数时，它将生成一个名为 AppNavigation.kt 的文件。
 */
fun main() {
    // 定义生成的代码要存放的目录
    // 路径是相对于项目根目录的，请确保它正确
    val outputDir = File("app-generated/src/main/java")
    outputDir.mkdirs() // 如果目录不存在，则创建它

    // 调用生成器函数
    generateAppNavigationFile(outputDir)
}

/**
 * 使用 KotlinPoet 生成 AppNavigation.kt 文件的核心函数。
 */
fun generateAppNavigationFile(outputDir: File) {
    val generatedPackageName = "com.template.generated"
    val generatedFileName = "AppNavigation"

    // --- 1. 定义所有需要用到的类和函数名称，便于引用 ---
    // AndroidX Compose & UI
    val composableAnnotation = ClassName("androidx.compose.runtime", "Composable")
    val modifier = ClassName("androidx.compose.ui", "Modifier")
    val scaffold = ClassName("androidx.compose.material3", "Scaffold")
    val experimentalMaterial3Api = ClassName("androidx.compose.material3", "ExperimentalMaterial3Api")
    val topAppBar = ClassName("androidx.compose.material3", "TopAppBar")
    val text = ClassName("androidx.compose.material3", "Text")
    val optInAnnotation = ClassName("kotlin", "OptIn")
    val paddingFun = MemberName("androidx.compose.foundation.layout", "padding")
    val spExt = MemberName("androidx.compose.ui.unit", "sp")

    // AndroidX Navigation
    val navController = ClassName("androidx.navigation", "NavController")
    val navGraphBuilder = ClassName("androidx.navigation", "NavGraphBuilder")
    val rememberNavControllerFun = MemberName("androidx.navigation.compose", "rememberNavController")
    val navHost = ClassName("androidx.navigation.compose", "NavHost")
    val composableFun = MemberName("androidx.navigation.compose", "composable")

    // 项目内部依赖
    val appRoutes = ClassName("com.template.core.navigation", "AppRoutes")
    val loginScreenFun = MemberName("com.template.feature.login.navigation", "loginScreen")


    // --- 2. 创建 NavGraphBuilder.customScreen() 扩展函数 ---
    val customScreenFun = FunSpec.builder("customScreen")
        .receiver(navGraphBuilder)
        .addAnnotation(
            AnnotationSpec.builder(optInAnnotation)
                .addMember("%T::class", experimentalMaterial3Api)
                .build()
        )
        .beginControlFlow("%M(route = %T.CUSTOM_ROUTER_ROUTE)", composableFun, appRoutes)
        .beginControlFlow(
            "%T(topBar = { %T(title = { %T(%S) }) }) { innerPadding ->",
            scaffold, topAppBar, text, "页面"
        )
        .addStatement(
            """
            %T(
                text = "Custom Screen",
                modifier = %T.%M(innerPadding),
                fontSize = 40.%M
            )
            """.trimIndent(),
            text, modifier, paddingFun, spExt
        )
        .endControlFlow()
        .endControlFlow()
        .build()

    // --- 3. 创建 NavController.navigateToCustom() 扩展函数 ---
    val navigateToCustomFun = FunSpec.builder("navigateToCustom")
        .receiver(navController)
        .addStatement("this.navigate(%T.CUSTOM_ROUTER_ROUTE)", appRoutes)
        .build()

    // --- 4. 创建 AppNavigation() Composable 函数 ---
    val appNavigationFun = FunSpec.builder("AppNavigation")
        .addAnnotation(composableAnnotation)
        .addStatement("val navController = %M()", rememberNavControllerFun)
        .addCode(
            CodeBlock.builder()
                .beginControlFlow(
                    "%T(navController = navController, startDestination = %T.LOGIN_ROUTE)",
                    navHost, appRoutes
                )
//                .addComment(" 系统自带的模块")
                .addStatement("%M(", loginScreenFun)
                .indent()
                .beginControlFlow("onLoginSuccess = {")
//                .addComment(" 登录成功后，导航到")
                .addStatement("navController.navigateToCustom()")
                .endControlFlow()
                .unindent()
                .addStatement(")")
                .addStatement("") // 添加一个空行以增加可读性
//                .addComment(" 自定义模块")
                .addStatement("customScreen()")
                .endControlFlow()
                .build()
        )
        .build()

    // --- 5. 组装成最终的文件 ---
    val fileSpec = FileSpec.builder(generatedPackageName, generatedFileName)
        .addFunction(appNavigationFun)
        .addFunction(navigateToCustomFun)
        .addFunction(customScreenFun)
        .build()

    // --- 6. 将文件写入磁盘 ---
    fileSpec.writeTo(outputDir)

    println("✅  Successfully generated ${generatedFileName}.kt in package $generatedPackageName")
}