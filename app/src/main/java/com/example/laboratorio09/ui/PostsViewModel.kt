package com.example.laboratorio09.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio09.data.PostModel
import com.example.laboratorio09.data.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<PostModel> = emptyList(),
    val selectedPost: PostModel? = null,
    val error: String? = null
)

class PostsViewModel(
    private val repository: PostRepository = PostRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPosts() {
        if (_uiState.value.posts.isNotEmpty()) return
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    posts = repository.getPosts()
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.localizedMessage ?: "No se pudieron cargar los posts"
                )
            }
        }
    }

    fun loadPost(id: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, selectedPost = null, error = null)
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    selectedPost = repository.getPost(id)
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.localizedMessage ?: "No se pudo cargar el post"
                )
            }
        }
    }
}
