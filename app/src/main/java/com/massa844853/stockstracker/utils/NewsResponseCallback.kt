package com.massa844853.stockstracker.utils

import com.massa844853.stockstracker.models.News

interface NewsResponseCallback {
    fun onResponse(newsList: MutableList<News>?, lastupdate: Long)
    fun onFailure(errorMessage: String?)
}