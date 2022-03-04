package com.massa844853.stockstracker.repository
import android.app.Application
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.models.Statistic
import com.massa844853.stockstracker.models.StatisticsResponse
import com.massa844853.stockstracker.utils.Constants
import com.massa844853.stockstracker.utils.StatisticsResponseCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import service.StatisticsApiService
import java.util.*
class StatisticsRepository(private val application: Application, private val statisticsResponseCallback: StatisticsResponseCallback) {
    fun start(symbol: String?) {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val statisticsApiService = retrofit.create(StatisticsApiService::class.java)
        val jsonCall = statisticsApiService.getStatistics(symbol)
        if (jsonCall != null) {
            jsonCall.enqueue(object : Callback<StatisticsResponse?> {
                override fun onResponse(call: Call<StatisticsResponse?>, response: Response<StatisticsResponse?>) {
                    val statisticsResponse = response.body()
                    if (response.body() != null && response.isSuccessful) {
                        val eleStatistic: MutableList<Statistic> = ArrayList()
                        eleStatistic.add(Statistic("Short Name", statisticsResponse!!.price.shortName))
                        eleStatistic.add(Statistic("Market State", statisticsResponse.price.marketState))
                        eleStatistic.add(Statistic("Quote Type", statisticsResponse.price.quoteType))
                        eleStatistic.add(Statistic("Market Cap", statisticsResponse.price.marketCap.fmt))
                        eleStatistic.add(Statistic("Shares Outstanding", statisticsResponse.defaultKeyStatistics.sharesOutstanding.fmt))
                        eleStatistic.add(Statistic("Beta", statisticsResponse.defaultKeyStatistics.beta.fmt))
                        eleStatistic.add(Statistic("Enterprise Value", statisticsResponse.defaultKeyStatistics.enterpriseValue.fmt))
                        eleStatistic.add(Statistic("52 Week Change", statisticsResponse.defaultKeyStatistics.get_52WeekChange().fmt))
                        eleStatistic.add(Statistic("Last Dividend Date", statisticsResponse.defaultKeyStatistics.lastDividendDate.fmt))
                        statisticsResponseCallback.onResponse(eleStatistic)
                    } else {
                        statisticsResponseCallback.onFailure(application.getString(R.string.error_search))
                    }
                }
                override fun onFailure(call: Call<StatisticsResponse?>, t: Throwable) {
                    Toast.makeText(application.applicationContext, "Error. " + t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}