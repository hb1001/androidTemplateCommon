package com.template.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.core.model.Post
import com.template.core.model.Reactions
import com.template.core.ui.theme.AppTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else {
                PostList(posts = uiState.posts)
            }
        }
    }
}

@Composable
private fun PostList(posts: List<Post>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            PostItem(post = post)
        }
    }
}

@Composable
private fun PostItem(post: Post) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview_Loading() {
    AppTheme {
        HomeScreenContent(uiState = HomeUiState(isLoading = true))
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview_Error() {
    AppTheme {
        HomeScreenContent(uiState = HomeUiState(error = "Failed to load posts"))
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview_Success() {
    val samplePosts = listOf(
        Post(1, "Post Title 1", "This is the body of post 1.", emptyList(), Reactions(5, 1), 100, 1),
        Post(2, "Post Title 2", "This is the body of post 2.", emptyList(), Reactions(10, 2), 200, 2)
    )
    AppTheme {
        HomeScreenContent(uiState = HomeUiState(posts = samplePosts))
    }
}