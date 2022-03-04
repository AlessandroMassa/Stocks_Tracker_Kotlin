package com.massa844853.stockstracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.massa844853.stockstracker.models.News

@Dao
interface NewsDao {
    @get:Query("SELECT * FROM news")
    val all: List<News?>?

    @Insert
    fun insertNewsList(newsList: List<News?>?)

    @Query("DELETE FROM news")
    fun deleteAll()
}