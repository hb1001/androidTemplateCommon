package com.template.feature.home.homelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.template.core.model.Post
import com.template.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PagingListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    val postsPagingFlow: Flow<PagingData<Post>> =
        postRepository.getPostsStream().cachedIn(viewModelScope)
}