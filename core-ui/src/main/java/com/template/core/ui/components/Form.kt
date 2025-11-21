package com.template.core.ui.components

import androidx.compose.runtime.Composable

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


// 定义表单字段的基类
sealed class FormField {
    abstract val label: String
    abstract val key: String // 用于标识字段，可选
}

// 文本输入类型
data class TextFormField(
    override val label: String,
    override val key: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val isMultiline: Boolean = false,
    val isReadOnly: Boolean = false
) : FormField()

// 数字输入类型
data class NumberFormField(
    override val label: String,
    override val key: String,
    val value: Number,
    val onValueChange: (String) -> Unit, // 接收 String 是为了方便处理空值或输入中状态
    val isReadOnly: Boolean = false
) : FormField()

// 标签列表输入类型 (特殊处理 List<String>)
data class TagsFormField(
    override val label: String,
    override val key: String,
    val tags: List<String>,
    val onTagsChange: (List<String>) -> Unit
) : FormField()

// 接收一个 List<FormField> 并把它们画出来
@Composable
fun GenericForm(
    fields: List<FormField>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        fields.forEach { field ->
            when (field) {
                is TextFormField -> RenderTextField(field)
                is NumberFormField -> RenderNumberField(field)
                is TagsFormField -> RenderTagsField(field)
            }
        }

        // 底部留白，防止被键盘遮挡
        Spacer(modifier = Modifier.height(100.dp))
    }
}

// --- 具体字段的渲染逻辑 ---

@Composable
private fun RenderTextField(field: TextFormField) {
    OutlinedTextField(
        value = field.value,
        onValueChange = field.onValueChange,
        label = { Text(field.label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = field.isReadOnly,
        enabled = !field.isReadOnly,
        minLines = if (field.isMultiline) 3 else 1,
        maxLines = if (field.isMultiline) 5 else 1
    )
}

@Composable
private fun RenderNumberField(field: NumberFormField) {
    OutlinedTextField(
        value = field.value.toString(),
        onValueChange = { newValue ->
            // 这里可以做简单的数字过滤，或者直接透传给上层处理
            if (newValue.all { it.isDigit() }) {
                field.onValueChange(newValue)
            }
        },
        label = { Text(field.label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = field.isReadOnly,
        enabled = !field.isReadOnly,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RenderTagsField(field: TagsFormField) {
    var tempTag by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = field.label, style = MaterialTheme.typography.labelMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // 输入框添加 Tag
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = tempTag,
                onValueChange = { tempTag = it },
                placeholder = { Text("输入标签") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(
                onClick = {
                    if (tempTag.isNotBlank() && tempTag !in field.tags) {
                        field.onTagsChange(field.tags + tempTag)
                        tempTag = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("添加")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 展示 Tags
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            field.tags.forEach { tag ->
                InputChip(
                    selected = true,
                    onClick = {
                        // 点击移除
                        field.onTagsChange(field.tags - tag)
                    },
                    label = { Text(tag) },
                    trailingIcon = {
                        Icon(Icons.Default.Close, contentDescription = "Remove")
                    }
                )
            }
        }
    }
}