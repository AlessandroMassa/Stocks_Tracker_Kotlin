package service

import com.massa844853.stockstracker.models.StatisticsResponse
import com.massa844853.stockstracker.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface StatisticsApiService {
    @Headers(Constants.API_KEY, Constants.API_HOST)
    @GET(Constants.STATISTICS_ENDPOINT)
    fun getStatistics(
            @Query("symbol") symbol: String?): Call<StatisticsResponse?>?
}