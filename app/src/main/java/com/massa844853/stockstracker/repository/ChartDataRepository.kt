package com.massa844853.stockstracker.repository
import android.app.Application
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.models.ChartDataResponse
import com.massa844853.stockstracker.models.StockPrice
import com.massa844853.stockstracker.utils.ChartDataResponseCallback
import com.massa844853.stockstracker.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import service.ChartDataApiService
import java.util.*
class ChartDataRepository(private val application: Application, private val chartDataResponseCallback: ChartDataResponseCallback) {
    fun start(symbol: String?, interval: String?, range: String?) {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val chartDataApiService = retrofit.create(ChartDataApiService::class.java)
        val jsonCall = chartDataApiService.getChartData(interval, symbol, range)
        if (jsonCall != null) {
            jsonCall.enqueue(object : Callback<ChartDataResponse?> {
                override fun onResponse(call: Call<ChartDataResponse?>, response: Response<ChartDataResponse?>) {
                    var chartDataResponse = response.body()
                    if (response.body() != null && response.isSuccessful) {
                        val stockPrices: MutableList<StockPrice> = ArrayList()
                        if (chartDataResponse != null) {
                            for (i in 0 until chartDataResponse.chart.result[0].timestamp.size) {
                                if(chartDataResponse.chart.result[0].indicators.adjclose != null) {
                                    stockPrices.add(StockPrice(Date(chartDataResponse.chart.result[0].timestamp[i] * 1000),
                                            chartDataResponse.chart.result[0].indicators.adjclose[0].adjclose[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].volume[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].high[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].low[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].open[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].close[i]))
                                }
                                else
                                {
                                    stockPrices.add(StockPrice(Date(chartDataResponse.chart.result[0].timestamp[i] * 1000),
                                            (-1).toDouble(),
                                            chartDataResponse.chart.result[0].indicators.quote[0].volume[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].high[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].low[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].open[i],
                                            chartDataResponse.chart.result[0].indicators.quote[0].close[i]))
                                }
                            }
                        }
                        chartDataResponse = null
                        chartDataResponseCallback.onResponsePrices(stockPrices)
                    } else {
                        chartDataResponseCallback.onFailure(application.getString(R.string.error_data))
                    }
                }
                override fun onFailure(call: Call<ChartDataResponse?>, t: Throwable) {
                    Toast.makeText(application.applicationContext, "Error. " + t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}