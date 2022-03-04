package com.massa844853.stockstracker.utils

import com.massa844853.stockstracker.models.Statistic

interface StatisticsResponseCallback {
    fun onResponse(eleStatistic: MutableList<Statistic>?)
    fun onFailure(errorMessage: String?)
}