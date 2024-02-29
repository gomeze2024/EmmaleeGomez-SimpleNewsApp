import android.content.Context
import com.example.simplenewsapp.News
import com.example.simplenewsapp.api.NewsInterceptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewsRepository private constructor() {
    private val newsApi: NewsApi
    private var cachedNews: List<News>? = null

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(NewsInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
        newsApi = retrofit.create<NewsApi>()
    }
    suspend fun getNews(): Flow<List<News>> {
        if (cachedNews == null) {
            val response = newsApi.fetchContents("business").string()
            cachedNews = parseJsonToNewsList(response)
        }
        return flow { emit(cachedNews!!) }
    }

    suspend fun getNews(category: String): Flow<List<News>> {
        val response = newsApi.fetchContents(category).string()
        cachedNews = parseJsonToNewsList(response)

        return flow { emit(cachedNews!!) }
    }

    suspend fun getNews(id: UUID, category: String): News? {
        return cachedNews?.find { it.id == id } ?: run {
            val response = newsApi.fetchContents(category).string()
            cachedNews = parseJsonToNewsList(response)
            cachedNews?.find { it.id == id }
        }
    }

    private fun parseJsonToNewsList(jsonResponse: String): List<News> {
        val jsonObject = JSONObject(jsonResponse)
        val articlesArray = jsonObject.optJSONArray("articles") ?: JSONArray()

        val newsList = mutableListOf<News>()

        for (i in 0 until articlesArray.length()) {
            val articleObject = articlesArray.optJSONObject(i)
            if (articleObject != null) {
                val news = parseJsonToNews(articleObject)
                newsList.add(news)
            }
        }

        return newsList
    }

    private fun parseJsonToNews(articleObject: JSONObject): News {
        val id = UUID.randomUUID()
        val title = articleObject.optString("title")
        val image = articleObject.optString("urlToImage")
        val author = articleObject.optString("author")
        val content = articleObject.optString("content")
        val url = articleObject.optString("url")

        val dateString = articleObject.optString("publishedAt")
        val date = try {
            if (dateString.isNotEmpty()) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(dateString)
            } else {
                Date()
            }
        } catch (e: ParseException) {
            Date()
        }

        return News(id, title, image, date, author, content, url)
    }

    companion object {
        private var INSTANCE: NewsRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = NewsRepository()
            }
        }

        fun get(): NewsRepository {
            return INSTANCE
                ?: throw IllegalStateException("NewsRepository must be initialized")
        }
    }
}
