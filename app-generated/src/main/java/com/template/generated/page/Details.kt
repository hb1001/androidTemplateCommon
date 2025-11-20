package com.template.generated.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.template.core.model.Post
import com.template.core.model.Reactions
import com.template.generated.component.GenericForm
import com.template.generated.component.NumberFormField
import com.template.generated.component.TagsFormField
import com.template.generated.component.TextFormField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditScreen() {
    // 1. 模拟初始数据
    val initialPost = remember {
        Post(
            id = 101,
            title = "Understanding Compose",
            body = "Jetpack Compose is Android’s modern toolkit for building native UI...",
            tags = listOf("Android", "Kotlin", "UI"),
            Reactions(5, 1),
            100,
            1
        )
    }

    // 2. 管理表单状态
    var currentPost by remember { mutableStateOf(initialPost) }

    // 3. 定义数据到表单字段的映射 (核心逻辑)
    // 每次 currentPost 变化时，这个列表都会重新构建
    val formFields = remember(currentPost) {
        listOf(
            // ID 通常是只读的
            NumberFormField(
                label = "id",
                key = "id",
                value = currentPost.id,
                onValueChange = {},
                isReadOnly = true
            ),
            TextFormField(
                label = "标题",
                key = "title",
                value = currentPost.title,
                onValueChange = { currentPost = currentPost.copy(title = it) }
            ),
            TextFormField(
                label = "内容",
                key = "body",
                value = currentPost.body,
                onValueChange = { currentPost = currentPost.copy(body = it) },
                isMultiline = true
            ),
            TagsFormField(
                label = "标签",
                key = "tags",
                tags = currentPost.tags,
                onTagsChange = { currentPost = currentPost.copy(tags = it) }
            ),
            // Reactions 比较复杂，这里演示将其作为只读信息展示，
            // 或者你可以专门写一个 ReactionFormField 类型
            NumberFormField(
                label = "点赞数",
                key = "likes",
                value = currentPost.reactions.likes,
                onValueChange = {},
                isReadOnly = true
            ),
            NumberFormField(
                label = "阅读数量",
                key = "views",
                value = currentPost.views,
                onValueChange = { str ->
                    // 安全转换 String -> Int
                    str.toIntOrNull()?.let { num ->
                        currentPost = currentPost.copy(views = num)
                    }
                }
            ),
            NumberFormField(
                label = "用户id",
                key = "userId",
                value = currentPost.userId,
                onValueChange = { /* 假设不允许修改作者 */ },
                isReadOnly = true
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("编辑") })
        },
        bottomBar = {
            Button(
                onClick = {
                    // 提交逻辑: 打印当前的 Post 对象
                    println("Submitting Post: $currentPost")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Save Changes")
            }
        }
    ) { paddingValues ->
        // 4. 调用通用表单组件
        GenericForm(
            fields = formFields,
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        )
    }
}