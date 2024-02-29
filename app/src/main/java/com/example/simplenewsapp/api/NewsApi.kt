import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun fetchContents(@Query("category") query : String) : ResponseBody
}