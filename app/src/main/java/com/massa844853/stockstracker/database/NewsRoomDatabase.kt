package com.massa844853.stockstracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.massa844853.stockstracker.models.News
import com.massa844853.stockstracker.utils.Constants
import java.util.concurrent.Executors

@Database(entities = [News::class], version = Constants.DATABASE_VERSION)
abstract class NewsRoomDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao?

    companion object {
        @Volatile
        private var INSTANCE: NewsRoomDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        @JvmStatic
        fun getDatabase(context: Context): NewsRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(NewsRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                NewsRoomDatabase::class.java, Constants.NEWS_DATABASE_NAME)
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}