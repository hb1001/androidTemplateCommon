package com.template.codegen


// codegen/src/main/kotlin/CodeGenerator.kt

import android.annotation.SuppressLint
import com.squareup.kotlinpoet.* // 确保导入 KotlinPoet 的所有相关类
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

// --- 1. 定义数据模型 ---

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PageDefinition(
    val pageInfo: PageInfo,
    val componentTree: TreeNode
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PageInfo(
    val fields: List<Field>,
    val methods: List<Method>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Field(
    val name: String,
    val type: String, // "number", "string", "boolean"
    val initValue: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Method(
    val name: String,
    val body: String // 简单起见，暂时用字符串表示
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TreeNode(
    val componentName: String,
    val props: Map<String, String>? = null,
    val events: List<Event>? = null,
    val children: List<TreeNode>? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Event(
    val type: String, // "onClick", "onLongClick"
    val actions: List<Action>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Action(
    val type: String, // "actionName"
    val actionName: String
)


// --- 2. 编写主函数来解析JSON ---

fun main() {
    println("JSON parsed successfully!")
//    // 读取JSON文件内容 (路径相对于模块根目录)
//    val jsonContent = File("codegen/src/main/resources/counter-page.json").readText()
//
//    // 使用kotlinx.serialization解析JSON
//    val pageDefinition = Json.decodeFromString<PageDefinition>(jsonContent)
//
//    // 打印出来验证一下
//    println("=============================================")
//    println("JSON parsed successfully!")
//    println("Page state variable: ${pageDefinition.pageInfo.fields.first().name}")
//    println("UI Root component: ${pageDefinition.componentTree.componentName}")
//
//    // TODO: 在这里开始我们的代码生成之旅
//
//    // 定义生成的代码要放在哪里
//    val outputDir = File("codegen/src/main/kotlin")
//    outputDir.mkdirs() // 创建目录，如果它不存在的话
//
//    // 调用 ViewModel 生成函数
//    generateViewModel(pageDefinition.pageInfo, outputDir)
//  500k = 305k + 81k+ 7k + 8k * 2 + 70k = 473k，差27k。约3~4月。 每年攒钱：5k*12+3.6k*12+70k=173k
//    generateComposableScreen(pageDefinition.componentTree, pageDefinition.pageInfo, outputDir)
////    generateComposableScreen(pageDefinition.componentTree, outputDir)
}

fun generateViewModel(pageInfo: PageInfo, outputDir: File) {
    val viewModelClassName = "CounterViewModel"
    val packageName = "generated"

    // 1. 生成属性 (Properties)
    // 遍历 JSON 中的 "fields"
    val properties = pageInfo.fields.map { field ->
        // 对于我们的 "count" 字段:
        // name = "count"
        // type = "number"
        // initValue = "0"

        // 将 JSON 类型映射到 Kotlin 类型
        val propertyType = when (field.type) {
            "number" -> Int::class // count 是一个 Int
            "string" -> String::class
            "boolean" -> Boolean::class
            else -> throw IllegalArgumentException("Unsupported field type: ${field.type}")
        }

        // 为了让 Compose 能够观察到状态变化，我们需要使用 MutableState<T>
        // 生成的代码会是：val count = mutableStateOf(0)
        val mutableStateTypeName = ClassName("androidx.compose.runtime", "MutableState")
        val parameterizedStateTypeName = mutableStateTypeName.parameterizedBy(propertyType.asTypeName())

        PropertySpec.builder(field.name, parameterizedStateTypeName)
            .initializer("mutableStateOf(${field.initValue})") // "mutableStateOf(0)"
            .build()
    }

    // 2. 生成方法 (Functions)
    // 遍历 JSON 中的 "methods"
    val functions = pageInfo.methods.map { method ->
        // 对于我们的 "increment" 方法:
        // name = "increment"
        // body = "this.count.value++"

        FunSpec.builder(method.name)
            .addModifiers(KModifier.PUBLIC)
            .addCode(method.body.replace("this.", "")) // 简单替换 "this." -> "count.value++"
            .build()
    }

    // 3. 生成 ViewModel 类 (Class)
    val viewModelClass = TypeSpec.classBuilder(viewModelClassName)
        .superclass(ClassName("androidx.lifecycle", "ViewModel"))
        .addProperties(properties) // 添加所有属性
        .addFunctions(functions)    // 添加所有方法
        .build()

    // 4. 生成文件 (File)
    val fileSpec = FileSpec.builder(packageName, "${viewModelClassName}.kt")
        .addType(viewModelClass)
        // KotlinPoet 会自动处理 import
        .addImport("androidx.compose.runtime", "mutableStateOf")
        .build()

    // 5. 将文件写入磁盘
    fileSpec.writeTo(outputDir)

    println("Successfully generated $viewModelClassName.kt")
}

fun generateComposableScreen(rootNode: TreeNode, pageInfo: PageInfo, outputDir: File) {
    val screenClassName = "CounterScreen"
    val viewModelClassName = "CounterViewModel" // 我们硬编码，也可以从 pageInfo 推断
    val packageName = "generated"

    // 1. 创建顶层 Composable 函数，并添加 ViewModel 参数
    // 生成：fun CounterScreen(viewModel: CounterViewModel = viewModel())
    val screenFunction = FunSpec.builder(screenClassName)
        .addAnnotation(ClassName("androidx.compose.runtime", "Composable"))
        .addModifiers(KModifier.PUBLIC)
        .addParameter(
            ParameterSpec.builder("viewModel", ClassName(packageName, viewModelClassName))
                .defaultValue("%T()", ClassName("androidx.lifecycle.viewmodel.compose", "viewModel"))
                .build()
        )
        // 递归调用，生成函数体内容
        .addCode(buildComposableNode(rootNode, "viewModel"))
        .build()

    // 2. 创建文件
    val fileSpec = FileSpec.builder(packageName, screenClassName)
        .addFunction(screenFunction)
        .build()

    // 3. 写入磁盘
    fileSpec.writeTo(outputDir)
    println("Successfully generated $screenClassName.kt")
}


// === 全新版本的核心递归函数 ===
private fun buildComposableNode(node: TreeNode, viewModelName: String): CodeBlock {
    val componentMapping = mapOf(
        "Column" to ClassName("androidx.compose.foundation.layout", "Column"),
        "Text" to ClassName("androidx.compose.material3", "Text"),
        "Button" to ClassName("androidx.compose.material3", "Button")
    )

    val composableName = componentMapping[node.componentName]
        ?: throw IllegalArgumentException("Unsupported component: ${node.componentName}")

    val builder = CodeBlock.builder()

    // 开始生成 Composable 调用，例如 "Column("
    builder.add("%T(\n", composableName)
    builder.indent() // 增加缩进

    // 1. 处理属性 (Props)
    node.props?.forEach { (key, value) ->
        // 将 "page.count.value" 这样的字符串替换为 "viewModel.count.value"
        val processedValue = value.replace("page.", "$viewModelName.")
        builder.addStatement("%L = %L,", key, processedValue)
    }

    // 2. 处理事件 (Events)
    node.events?.forEach { event ->
        val eventName = when (event.type) {
            "onClick" -> "onClick"
            else -> throw IllegalArgumentException("Unsupported event type: ${event.type}")
        }

        // 生成 onClick = { ... }
        builder.addStatement("%L = {", eventName)
        builder.indent()
        event.actions.forEach { action ->
            if (action.type == "actionName") {
                // 生成 viewModel.increment()
                builder.addStatement("%L.%L()", viewModelName, action.actionName)
            }
        }
        builder.unindent()
        builder.addStatement("},")
    }

    builder.unindent() // 减少缩进

    // 3. 处理子节点 (Children)
    if (node.children.isNullOrEmpty()) {
        builder.addStatement(")") // 如果没有子节点，直接闭合括号
    } else {
        builder.addStatement(") {") // 如果有子节点，用花括号包裹
        builder.indent()
        node.children.forEach { child ->
            builder.add(buildComposableNode(child, viewModelName))
        }
        builder.unindent()
        builder.addStatement("}")
    }

    return builder.build()
}