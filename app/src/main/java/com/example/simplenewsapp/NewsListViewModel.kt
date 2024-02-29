import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenewsapp.News
import com.example.simplenewsapp.NewsCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "NewsListViewModel"

class NewsListViewModel : ViewModel() {
    private val newsRepository = NewsRepository.get()

    private val _news: MutableStateFlow<List<News>> = MutableStateFlow(emptyList())
    val news: StateFlow<List<News>>
        get() = _news.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                newsRepository.getNews().collect {
                    _news.value = it
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch news list data")
            }
        }
    }

    fun updateNews(category: NewsCategory) {
        viewModelScope.launch {
            try {
                val categoryString = when (category) {
                    is NewsCategory.Business -> "business"
                    is NewsCategory.Entertainment -> "entertainment"
                    is NewsCategory.General -> "general"
                    is NewsCategory.Health -> "health"
                    is NewsCategory.Science -> "science"
                    is NewsCategory.Sports -> "sports"
                    is NewsCategory.Technology -> "technology"
                }
                newsRepository.getNews(categoryString).collect {
                    _news.value = it
                    Log.d(TAG, "News updated: ${it.size} items ${it[0]}")
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch news list data")
            }
        }
    }


}
