package com.template.feature.solver

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt // <-- 导入相机图标
import androidx.compose.material.icons.filled.PhotoLibrary // <-- 导入相册图标
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// --- UI部分 ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolverScreen(viewModel: SolverViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 拍照和相册选择逻辑
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempImageUri?.let { uri ->
                context.uriToFile(uri)?.let { file ->
                    viewModel.onImageSelected(file)
                }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.uriToFile(it)?.let { file ->
                viewModel.onImageSelected(file)
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("拍题助手") }) },
        bottomBar = {
            SolverBottomBar(
                onTakePhoto = {
                    val uri = context.createImageUri()
                    tempImageUri = uri
                    cameraLauncher.launch(uri)
                },
                onChooseFromGallery = { galleryLauncher.launch("image/*") }
            )
        }
    ) { paddingValues ->
        ChatList(
            messages = uiState.messages,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

// ... ChatList, ChatBubble, SolverBottomBar Composable ...

// --- 权限和文件处理辅助函数 ---
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SolverBottomBar(onTakePhoto: () -> Unit, onChooseFromGallery: () -> Unit) {
    // 请求相机权限
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    BottomAppBar(
        actions = {
            IconButton(onClick = {
                if (cameraPermissionState.status.isGranted) {
                    onTakePhoto()
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Take Photo")
            }
            IconButton(onClick = onChooseFromGallery) {
                // 使用正确的图标
                Icon(Icons.Default.PhotoLibrary, contentDescription = "Choose from Gallery")
            }
        }
    )
}

// 在一个单独的工具文件中创建这些
fun Context.createImageUri(): Uri {
    val file = createImageFile()
    return FileProvider.getUriForFile(
        this,
        "${this.packageName}.provider", // 需要在 Manifest 中配置 FileProvider
        file
    )
}

//private fun Context.createImageFile(): File {
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//    val imageFileName = "JPEG_" + timeStamp + "_"
//    return File.createTempFile(imageFileName, ".jpg", this.externalCacheDir)
//}

// 这个函数位于 feature-solver 模块中
// 你也可以考虑把它移到 core-common 模块的一个工具类里
private fun Context.createImageFile(): File {
    // 使用 filesDir 或 cacheDir 都可以，但要与 file_paths.xml 对应
    // externalCacheDir 是一个不错的选择，因为它会在应用卸载时被系统清理
    val storageDir: File? = this.externalCacheDir
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg",               /* suffix */
        storageDir            /* directory */
    )
}

fun Context.uriToFile(uri: Uri): File? {
    return try {
        val inputStream = this.contentResolver.openInputStream(uri)
        val file = createImageFile()
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun ChatList(messages: List<ChatMessage>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    // 当新消息添加时，自动滚动到底部
    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }

    if (messages.isEmpty()) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("拍照识题，秒出答案", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            when (message) {
                is ChatMessage.UserMessage -> UserChatBubble(message)
                is ChatMessage.SystemMessage -> SystemChatBubble(message)
            }
        }
    }
}

@Composable
fun UserChatBubble(message: ChatMessage.UserMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 250.dp) // 限制图片最大宽度
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = message.imageFile),
                contentDescription = "User question image",
                modifier = Modifier.aspectRatio(1f), // 保持正方形
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun SystemChatBubble(message: ChatMessage.SystemMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(
                    if (message.isError) MaterialTheme.colorScheme.errorContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            if (message.isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = message.text, style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isError) MaterialTheme.colorScheme.onErrorContainer else LocalContentColor.current
                )
            }
        }
    }
}