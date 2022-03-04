package com.massa844853.stockstracker.repository
import android.app.Application
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.database.NewsDao
import com.massa844853.stockstracker.database.NewsRoomDatabase
import com.massa844853.stockstracker.database.NewsRoomDatabase.Companion.getDatabase
import com.massa844853.stockstracker.models.News
import com.massa844853.stockstracker.models.NewsResponse
import com.massa844853.stockstracker.utils.Constants
import com.massa844853.stockstracker.utils.NewsResponseCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import service.NewsApiService
import java.util.*
class NewsRepository(private val application: Application, private val newsResponseCallback: NewsResponseCallback
) {
    private var newsRoomDatabase: NewsRoomDatabase
    private val newsDao: NewsDao
    fun start(lastUpdate: Long) {
        newsRoomDatabase = getDatabase(application)!!;
        if (System.currentTimeMillis() - lastUpdate > Constants.FRESH_TIMEOUT) {
            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            val newsApiService = retrofit.create(NewsApiService::class.java)
            val jsonCall = newsApiService.getNews("generalnews", "US")
            if (jsonCall != null) {
                jsonCall.enqueue(object : Callback<NewsResponse?> {
                    override fun onResponse(call: Call<NewsResponse?>, response: Response<NewsResponse?>) {
                        val newsResponse = response.body()
                        if (response.body() != null && response.isSuccessful) {
                            val newsListImg = Arrays.asList(*newsResponse!!.items.result)
                            val newsList: MutableList<News>? = ArrayList()
                            for (item in newsListImg) {
                                try {
                                    if (newsList != null) {
                                        newsList.add(News(item.title, item.link, item.publisher, item.published_at, item.main_image.resolutions[0].url))
                                    }
                                } catch (e: Exception) {
                                }
                            }
                            if (newsList != null) {
                                saveDataInDatabase(newsList)
                            }
                            newsResponseCallback.onResponse(newsList, System.currentTimeMillis())
                        } else {
                            newsResponseCallback.onFailure(application.getString(R.string.error_news))
                        }
                    }
                    override fun onFailure(call: Call<NewsResponse?>, t: Throwable) {
                        Toast.makeText(application.applicationContext, "Error. " + t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        } else {
            readDataFromDatabase(lastUpdate)
        }
    }
    private fun saveDataInDatabase(newsList: List<News?>) {
        val runnable = Runnable {
            newsDao!!.deleteAll()
            newsDao.insertNewsList(newsList)
        }
        Thread(runnable).start()
    }
    private fun readDataFromDatabase(lastUpdate: Long) {
        val runnable = Runnable { newsResponseCallback.onResponse(newsDao!!.all as MutableList<News>?, lastUpdate) }
        Thread(runnable).start()
    }
    init {
        newsRoomDatabase = getDatabase(application)!!
        newsDao = newsRoomDatabase.newsDao()!!
    }
}