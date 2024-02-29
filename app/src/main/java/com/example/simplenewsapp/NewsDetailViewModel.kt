package com.example.simplenewsapp

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "NewsDetailViewModel"

class NewsDetailViewModel(newsId: UUID) : ViewModel() {
    private val newsRepository = NewsRepository.get()

    private val _news: MutableStateFlow<News?> = MutableStateFlow(null)
    val news: StateFlow<News?> = _news.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _news.value = newsRepository.getNews(newsId, "business")
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch news detail data")
            }
        }
    }

}

class NewsDetailViewModelFactory(
    private val newsId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsDetailViewModel(newsId) as T
    }
}
