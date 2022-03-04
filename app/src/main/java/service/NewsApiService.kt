package service

import com.massa844853.stockstracker.models.NewsResponse
import com.massa844853.stockstracker.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//interfaccia per ottenere le news di mercato dal servizio rapidapi
interface NewsApiService {
    @Headers(Constants.API_KEY, Constants.API_HOST)
    @GET(Constants.NEWS_ENDPOINT)
    fun getNews(
            @Query("category") category: String?,
            @Query("region") region: String?): Call<NewsResponse?>?
}