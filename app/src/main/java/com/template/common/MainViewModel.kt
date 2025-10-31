package com.template.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.data.network.apiservice.PostApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val postApiService: PostApiService
) : ViewModel() {

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                Timber.d("发起网络请求...")
                val response = postApiService.getPosts()
                Timber.d("请求成功! 获取到 ${response.posts.size} 条帖子。")
                Timber.d("第一条帖子的标题是: ${response.posts.firstOrNull()?.title}")
            } catch (e: Exception) {
                Timber.e(e, "网络请求失败!")
            }
        }
    }
}